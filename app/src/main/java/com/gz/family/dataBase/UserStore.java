package com.gz.family.dataBase;

import android.text.TextUtils;

import com.gz.family.model.User;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;
import java.util.Map;

/**
 * some operation about the users of local database
 * but only operate the first level of the user's reletivies
 * Created by host on 2016/2/8.
 */
public class UserStore {
    public static void storeLocalUser(User user) {
        if (user == null)
            return;
        DBhelper.getInstance().clearDb(User.class);
        DBhelper.getInstance().clearDb(UserMap.class);
        DBhelper.getInstance().add(user);
        for (Map.Entry<String, User> rel : user.getRelatives().entrySet()) {
            DBhelper.getInstance().add(rel.getValue());//add the relative user class first
            UserMap userMap = new UserMap();
            userMap.masterId = user.getId();
            userMap.slaveId = rel.getValue().getId();
            userMap.relation = rel.getKey();
            DBhelper.getInstance().add(userMap);//add the relation table connect to relative
        }
    }

    public static User getLocalUser(boolean lazzy) {
        List<User> userList = DBhelper.getInstance().getAll(User.class);
        if (userList == null || userList.size() == 0) {
            return null;
        }
        //get the first user, who is the owner
        User local = userList.get(0);
        if (lazzy) {
            //if lazzy is true, then won't add the relatives
            return local;
        }
        List<UserMap> mapList = DBhelper.getInstance().getAll(UserMap.class);
        for (UserMap map : mapList) {
            if (!map.masterId.equals(local.getId())) {
                //if the master is not owner, skip
                continue;
            }
            for (User u : userList) {
                if (map.slaveId.equals(u.getId())) {
                    local.getRelatives().put(map.relation, u);
                }
            }
        }
        return local;
    }

    public static User getUser(String id) {
        return DBhelper.getInstance().getById(User.class,id);
    }

    public void addReletive(User user, String relation) {
        if (user == null || TextUtils.isEmpty(user.getId())) {
            return;
        }
        List<UserMap> map = DBhelper.getInstance().getAll(UserMap.class);
        //check if the relation is existed in local database
        for (UserMap m : map) {
            if (m.relation.equals(relation)) {
                return;
            }
        }
        User owner = getLocalUser(true);

        //first store the relative user
        DBhelper.getInstance().add(user);
        //then store the relation map
        UserMap userMap = new UserMap();
        userMap.masterId = owner.getId();
        userMap.slaveId = user.getId();
        userMap.relation = relation;
        DBhelper.getInstance().add(userMap);
    }

    public void updateRelatives(Map<String, User> userMap) {
        if (userMap == null) {
            return;
        }
        User owner = getLocalUser(true);
        owner.setRelatives(userMap);
        storeLocalUser(owner);
    }

    /**
     * the inner class for the relation of relative
     * for save in local database by orm
     */

    @DatabaseTable(tableName = "relative")
    public static class UserMap {
        @DatabaseField(columnName = "relation")
        public String relation;
        @DatabaseField(columnName = "master_id")
        public String masterId;
        @DatabaseField(columnName = "slaveId")
        public String slaveId;
    }
}
