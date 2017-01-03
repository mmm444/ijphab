package mmm444.ijphab;

import mmm444.ijphab.model.QueryResponse;
import mmm444.ijphab.model.StatusesResponse;
import mmm444.ijphab.model.WhoamiResponse;
import com.intellij.tasks.impl.RequestFailedException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

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
        String json = "{\"result\":{\"PHID-TASK-tdlqveicvcavncwigjog\":{\"id\":\"864\",\"phid\":\"PHID-TASK-tdlqveicvcavncwigjog\",\"authorPHID\":\"PHID-USER-zj3eydsvzhl66iuhx5bl\",\"ownerPHID\":\"PHID-USER-zj3eydsvzhl66iuhx5bl\",\"ccPHIDs\":[\"PHID-USER-zj3eydsvzhl66iuhx5bl\"],\"status\":\"open\",\"statusName\":\"Open\",\"isClosed\":false,\"priority\":\"Normal\",\"priorityColor\":\"orange\",\"title\":\"Debug DLL\",\"description\":\"DO!\",\"projectPHIDs\":[\"PHID-PROJ-xyazbl3w7i2sxpmb3lej\"],\"uri\":\"https:\\/\\/example.com\\/T864\",\"auxiliary\":[],\"objectName\":\"T864\",\"dateCreated\":\"1481125974\",\"dateModified\":\"1481125974\",\"dependsOnTaskPHIDs\":[]}},\"error_code\":null,\"error_info\":null}";
        QueryResponse res = PhabricatorUtil.GSON.fromJson(json, QueryResponse.class);
        res.checkOk();
        Map<String, QueryResponse.TaskData> data = res.getResult();
        assertNotNull(data);
        assertEquals(1, data.size());

        QueryResponse.TaskData td = data.values().iterator().next();
        assertEquals("864", td.getId());
        assertFalse(td.isClosed());
        assertEquals(Collections.singletonList("PHID-PROJ-xyazbl3w7i2sxpmb3lej"), td.getProjectPHIDs());
        assertEquals(new Date(1481125974000L), td.getDateCreated());
        assertEquals(new Date(1481125974000L), td.getDateModified());
    }

    @Test public void testStatusesDeserialization() {
        String json = "{\"result\":{\"defaultStatus\":\"open\",\"defaultClosedStatus\":\"resolved\",\"duplicateStatus\":\"duplicate\",\"openStatuses\":[\"open\",\"progress\"],\"closedStatuses\":{\"2\":\"resolved\",\"3\":\"wontfix\",\"4\":\"invalid\",\"5\":\"duplicate\",\"6\":\"spite\"},\"allStatuses\":[\"open\",\"progress\",\"resolved\",\"wontfix\",\"invalid\",\"duplicate\",\"spite\"],\"statusMap\":{\"open\":\"Open\",\"progress\":\"In Progress\",\"resolved\":\"Resolved\",\"wontfix\":\"Wontfix\",\"invalid\":\"Invalid\",\"duplicate\":\"Duplicate\",\"spite\":\"Spite\"}},\"error_code\":null,\"error_info\":null}";
        StatusesResponse res = PhabricatorUtil.GSON.fromJson(json, StatusesResponse.class);
        res.checkOk();
        for (String st : Arrays.asList("open", "progress", "resolved", "wontfix", "invalid", "duplicate", "spite")) {
            assertTrue(st, res.getResult().getStatusMap().containsKey(st));
        }
        System.out.println(res.getResult().getStatusMap());
    }
}
