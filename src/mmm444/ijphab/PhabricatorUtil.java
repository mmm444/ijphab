package mmm444.ijphab;

import com.google.gson.*;

import java.util.Date;

class PhabricatorUtil {
  static final Gson GSON = buildGson();

  private static Gson buildGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Date.class,
                                    (JsonDeserializer<Date>)(jsonElement, type, ctx) -> new Date(jsonElement.getAsLong() * 1000L));
    return gsonBuilder.create();
  }
}
