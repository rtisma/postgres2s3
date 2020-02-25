package bio.overture.backup.postgres2s3.util;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

@Builder
@RequiredArgsConstructor
public class StreamGobbler implements  Runnable {

  @NonNull private final InputStream inputStream;
  @NonNull private final Consumer<String> consumer;

  @Override
  public void run() {
    new BufferedReader(new InputStreamReader(inputStream)).lines()
        .forEach(consumer);
  }

}
