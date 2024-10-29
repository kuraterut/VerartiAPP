import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.*;

public class ParseJSON{
	public static ArrayList<String[]> parseMasterResourceList(JSONObject data){
		ArrayList<String[]> result = new ArrayList<>();

		JSONArray jsonArr = (JSONArray) data.get("data");
        Iterator itr = jsonArr.iterator();
        while (itr.hasNext()) {
            JSONObject jsonObjArr = (JSONObject) itr.next();
            String[] strArr = new String[2];
            strArr[0] = (String)jsonObjArr.get("name");
            strArr[1] = (String)jsonObjArr.get("description");
            result.add(strArr);
        }
        return result;
	}

    public static ArrayList<String[]> parseTimetableByYMInfo(JSONObject data){
        ArrayList<String[]> result = new ArrayList<>();

        JSONArray jsonArr = (JSONArray) data.get("data");
        Iterator itr = jsonArr.iterator();
        while (itr.hasNext()) {
            JSONObject jsonObjArr = (JSONObject) itr.next();
            String[] strArr = new String[2];
            strArr[0] = (String)jsonObjArr.get("count");
            strArr[1] = (String)jsonObjArr.get("time");
            result.add(strArr);
        }
        return result;
    }
}