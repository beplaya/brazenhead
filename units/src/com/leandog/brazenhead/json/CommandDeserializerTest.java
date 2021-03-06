package com.leandog.brazenhead.json;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.*;

import com.google.brazenhead.gson.*;
import com.leandog.brazenhead.commands.*;
import com.leandog.brazenhead.commands.Command.Target;

public class CommandDeserializerTest {

    Gson gson;

    @Before
    public void setUp() {
        gson = new GsonBuilder()
            .registerTypeAdapter(Command.class, new CommandDeserializer())
            .create();
    }

    @Test
    public void itOnlyHasTheExpectedFieldsToReflectUpon() {
        List<String> fieldNames = new ArrayList<String>();
        for (final Field field : Command.class.getDeclaredFields()) {
            fieldNames.add(field.getName());
        }
        
        assertThat(fieldNames, containsInAnyOrder("name", "arguments", "target", "variable"));
    }

    @Test
    public void itCanGetAnEmptyObject() {
        Command actualCommand = deserialize("{}");
        assertThat(actualCommand.getName(), is(nullValue()));
        assertThat(actualCommand.getArguments(), is(new Object[0]));
    }

    @Test
    public void itCanGetAMethodName() {
        Command actualCommand = deserialize("{name: \"theMethodName\"}");
        assertThat(actualCommand.getName(), is("theMethodName"));
    }

    @Test
    public void itCanGetNullArguments() {
        Command actualCommand = deserialize("{arguments: null}");
        assertThat(actualCommand.getArguments(), is(new Object[0]));
    }

    @Test
    public void itCanHandleIfArgumentsIsNotAnArray() {
        Command actualCommand = deserialize("{arguments: 3}");
        assertThat(actualCommand.getArguments(), is(new Object[0]));
    }

    @Test
    public void itCanGetAnIntegerArgument() {
        Command actualCommand = deserialize("{arguments: [3]}");
        assertThat(actualCommand.getArguments(), is(new Object[] { 3 }));
    }

    @Test
    public void itCanGetABooleanArgument() {
        Command actualCommand = deserialize("{arguments: [true]}");
        assertThat(actualCommand.getArguments(), is(new Object[] { true }));
    }

    @Test
    public void itCanGetAFloatArgument() {
        Command actualCommand = deserialize("{arguments: [3.0]}");
        assertThat(actualCommand.getArguments(), is(new Object[] { 3.0f }));
    }

    @Test
    public void itCanGetADoubleArgument() {
        Double maxDouble = Double.MAX_VALUE;
        Command actualCommand = deserialize("{arguments: [" + maxDouble.toString() + "]}");
        assertThat(actualCommand.getArguments(), is(new Object[] { maxDouble }));
    }

    @Test
    public void itCanGetAStringArgument() {
        Command actualCommand = deserialize("{arguments: [\"some string\"]}");
        assertThat(actualCommand.getArguments(), is(new Object[] { "some string" }));
    }
    
    @Test
    public void itCanParseOutTheDesiredTarget() {
        Command actualCommand = deserialize("{target: 'Robotium'}");
        assertThat(actualCommand.getTarget(), is(Target.Robotium));
    }
    
    @Test
    public void targetsResortToTheDefault() {
        Command actualCommand = deserialize("{}");
        assertThat(actualCommand.getTarget(), is(Target.LastResultOrRobotium));
    }
    
    @Test
    public void itCanParseOutTheDesiredVariable() {
        Command actualCommand = deserialize("{variable: '@@var_name@@'}");
        assertThat(actualCommand.variableName(), is("@@var_name@@"));
    }

    private Command deserialize(final String json) {
        return gson.fromJson(json, Command.class);
    }

}
