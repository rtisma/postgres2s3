package bio.overture.backup.postgres2s3.util;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class CollectionUtils {

  public static Map<String, String> convertToStringStringMap(Map<String, Object> map){
    return map.entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey, y -> y.getValue().toString()));
  }


}
