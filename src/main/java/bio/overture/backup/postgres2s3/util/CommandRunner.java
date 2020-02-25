package bio.overture.backup.postgres2s3.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static bio.overture.backup.postgres2s3.util.CollectionUtils.convertToStringStringMap;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.stream;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
@Value
@Builder
public class CommandRunner {

  private final static char NEWLINE = '\n';
  private static final Joiner WHITESPACE = Joiner.on(" ");

  @NonNull private final String command;

  public CommandResult executeArgs(Object ... args) throws IOException, InterruptedException {
    return executeArgsWithEnv(Map.of(), args);
  }

  public CommandResult executeArgsWithEnv(@NonNull Map<String,Object> envVars, Object ... args) throws IOException, InterruptedException {
    val stringArgs = stream(args).map(Object::toString).collect(toUnmodifiableList());
    return execute(convertToStringStringMap(envVars), stringArgs);
  }

  public CommandResult execute(@NonNull String args) throws IOException, InterruptedException {
    return execute(Map.of(), args);
  }

  public CommandResult execute(@NonNull Map<String, Object> envVars, @NonNull String args) throws IOException, InterruptedException {
    return execute(convertToStringStringMap(envVars), List.of(args.split("\\s+")));
  }

  public CommandResult execute(@NonNull Map<String, String> envVars, @NonNull List<String> args) throws IOException, InterruptedException {
    val pb = new ProcessBuilder();
    val finalCommand = Lists.<String>newArrayList(command);
    finalCommand.addAll(args);
    pb.environment().putAll(envVars);
    pb.command(finalCommand);
    pb.redirectErrorStream(true);
    val process = pb.start();
    val sb = new StringBuffer();
    val streamGobbler = StreamGobbler.builder()
        .inputStream(process.getInputStream())
        .consumer(x -> {
          sb.append(x);
          sb.append(NEWLINE);
        })
        .build();
    newSingleThreadExecutor().submit(streamGobbler);
    int exitCode = process.waitFor();
    log.trace("OUTPUT: \n{}", sb.toString());
    log.trace("ExitCode: {}", exitCode);
    return CommandResult.builder()
        .command(WHITESPACE.join(finalCommand))
        .output(sb.toString())
        .exitCode(exitCode)
        .build();
  }

  @Value
  @Builder
  public static class CommandResult{
    @NonNull private final String command;
    @NonNull private final String output;
    private final int exitCode;
  }

  private static boolean isWindows(){
    return System.getProperty("os.name").toLowerCase().startsWith("windows");
  }

}
