package com.leandog.brazenhead.json;

import java.lang.reflect.Type;

import android.widget.TextView;

import com.google.brazenhead.gson.*;

public class TextViewJsonSerializer implements JsonSerializer<TextView> {

    @Override
    public JsonElement serialize(TextView theTextView, Type type, JsonSerializationContext context) {
        return new Gson().toJsonTree(new JsonTextView(theTextView));
    }

}
