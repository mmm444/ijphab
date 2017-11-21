package mmm444.ijphab;

import mmm444.ijphab.model.SearchResponse;
import mmm444.ijphab.model.StatusesResponse;
import mmm444.ijphab.model.WhoamiResponse;
import com.intellij.tasks.impl.RequestFailedException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PhabricatorJsonTest {
    @Test(expected = RequestFailedException.class)
    public void testInvalidWhoamiDeserialization() {
        String json = "{\"result\":null,\"error_code\":\"ERR-INVALID-AUTH\",\"error_info\":\"API token \\\"api-xxxxx\\\" has the wrong length. API tokens should be 32 characters long.\"}";
        WhoamiResponse res = PhabricatorUtil.GSON.fromJson(json, WhoamiResponse.class);
        res.checkOk();
    }

    @Test
    public void testAuthPingDeserialization() {
        String json = "{\"result\": {\"x\": \"9e3ffa6b5f6c\"},\"error_code\":null,\"error_info\":null}";
        WhoamiResponse res = PhabricatorUtil.GSON.fromJson(json, WhoamiResponse.class);
        res.checkOk();
        assertTrue(res.getResult().containsKey("x"));
    }

    @Test
    public void testQueryDeserializetion() {
        String json = "{\"result\":{\"data\": [{\"id\": 975,\"type\": \"TASK\",\"phid\": \"PHID-TASK-2otcnfe7y2o7ov5liszp\",\"fields\": {\"name\": \"name\",\"description\": {\"raw\": \"desc\"},\"authorPHID\": \"PHID-USER-az3yo324ahh2nfi6uqij\",\"ownerPHID\": \"PHID-USER-mja2zuovx3axw2nkvlbl\",\"status\": {\"value\": \"resolved\",\"name\": \"Resolved\",\"color\": null},\"priority\": {\"value\": 80,\"subpriority\": 0,\"name\": \"High\",\"color\": \"red\"},\"points\": null,\"subtype\": \"default\",\"spacePHID\": null,\"dateCreated\": 1498162624,\"dateModified\": 1498211502,\"policy\": {\"view\": \"users\",\"interact\": \"users\",\"edit\": \"users\"}},\"attachments\": {\"projects\": {\"projectPHIDs\": [\"PHID-PROJ-xyazbl3w7i2sxpmb3lej\",\"PHID-PROJ-2tvii2xte73tvvkmlqqj\"]}}}], \"maps\": {},\"query\": {\"queryKey\": \"all\"},\"cursor\": {\"limit\": 100,\"after\": null,\"before\": null,\"order\": null}},\"error_code\":null,\"error_info\":null}";
        SearchResponse res = PhabricatorUtil.GSON.fromJson(json, SearchResponse.class);
        res.checkOk();
        List<SearchResponse.TaskData> data = res.getData();
        assertNotNull(data);
        assertEquals(1, data.size());

        SearchResponse.TaskData td = data.iterator().next();
        assertEquals("975", td.getId());
        assertEquals(Arrays.asList("PHID-PROJ-xyazbl3w7i2sxpmb3lej", "PHID-PROJ-2tvii2xte73tvvkmlqqj"), td.getProjectPHIDs());
        assertEquals(new Date(1498162624000L), td.getDateCreated());
        assertEquals(new Date(1498211502000L), td.getDateModified());
    }

    @Test public void testStatusesDeserialization() {
        String json = "{\"result\":{\"data\": [{\"name\": \"Open\",\"value\": \"open\",\"closed\": false,\"special\": \"default\"},{\"name\": \"In Progress\",\"value\": \"progress\",\"closed\": false},{\"name\": \"Resolved\",\"value\": \"resolved\",\"closed\": true,\"special\": \"closed\"},{\"name\": \"Wontfix\",\"value\": \"wontfix\",\"closed\": true},{\"name\": \"Invalid\",\"value\": \"invalid\",\"closed\": true},{\"name\": \"Duplicate\",\"value\": \"duplicate\",\"closed\": true,\"special\": \"duplicate\"},{\"name\": \"Spite\",\"value\": \"spite\",\"closed\": true}]},\"error_code\":null,\"error_info\":null}";
        StatusesResponse res = PhabricatorUtil.GSON.fromJson(json, StatusesResponse.class);
        res.checkOk();
        for (String st : Arrays.asList("open", "progress", "resolved", "wontfix", "invalid", "duplicate", "spite")) {
            assertTrue(st, res.getData().stream().anyMatch(s -> s.getValue().equals(st)));
        }
    }
}
