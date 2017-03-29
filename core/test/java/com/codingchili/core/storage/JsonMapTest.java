package com.codingchili.core.storage;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Paths;

import com.codingchili.core.files.JsonFileStore;
import com.codingchili.core.files.exception.NoSuchResourceException;
import com.codingchili.core.protocol.Serializer;
import com.codingchili.core.testing.MapTestCases;
import com.codingchili.core.testing.StorageObject;

import static com.codingchili.core.configuration.CoreStrings.*;

/**
 * @author Robin Duda
 *         <p>
 *         Tests for the storage providers in core. Reuse these tests when new
 *         storage subsystems are implemented using the StorageLoader.
 */
@RunWith(VertxUnitRunner.class)
public class JsonMapTest extends MapTestCases {
    @Before
    public void setUp(TestContext test) {
        super.setUp(test, JsonMap.class);
    }

    @After
    public void tearDown(TestContext test) {
        super.tearDown(test);
    }

    @Ignore("Map currently defaults to not saving, will reconsider.")
    @Test
    public void testMapSaved(TestContext test) {
        Async async = test.async();
        StorageObject storable = new StorageObject("the_id", 21);

        store.put(storable, result -> context.timer(500, event -> {
            try {
                JsonObject db = JsonFileStore.readObject(
                        Paths.get(getDBPath(getDBIdentifier(DB, COLLECTION))).toString());

                StorageObject second = Serializer.unpack(
                        db.getJsonObject(storable.getName()), StorageObject.class);

                test.assertEquals(storable, second);
                async.complete();
            } catch (NoSuchResourceException e) {
                test.fail(e);
            }
        }));
    }

    @Ignore("Not supported yet, saving the map breaks this.")
    public void testStorageIsShared(TestContext test) {
    }
}
