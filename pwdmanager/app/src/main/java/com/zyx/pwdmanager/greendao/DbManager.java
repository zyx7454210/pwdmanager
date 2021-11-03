package com.zyx.pwdmanager.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class DbManager {

    private static final String PASSWORD = "greenDaoYYDS";
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper helper;//获取Helper对象
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * DaoMaster
     */
    private DaoMaster daoMaster;
    /**
     * DaoSession
     */
    private DaoSession daoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private ItemDao itemDao;

    private GroupDao groupDao;

    private static DbManager DBMANAGER;

    /**
     * 获取单例
     */
    public static DbManager getInstance(Context context) {
        if (DBMANAGER == null) {
            synchronized (DbManager.class) {
                if (DBMANAGER == null) {
                    DBMANAGER = new DbManager(context);
                }
            }
        }
        return DBMANAGER;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DbManager(Context context) {
        this.context = context;
        helper = new DaoMaster.DevOpenHelper(context, "db.db", null);
        daoMaster = new DaoMaster(getWritableDatabase());
        daoSession = daoMaster.newSession();
        itemDao = daoSession.getItemDao();
        groupDao = daoSession.getGroupDao();
    }

    /**
     * 获取可读数据库
     */
    private Database getReadableDatabase() {
        if (helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, "db.db", null);
        }
        return helper.getEncryptedReadableDb(PASSWORD);
    }

    /**
     * 获取可写数据库
     */
    private Database getWritableDatabase() {
        if (helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, "db.db", null);
        }
        return helper.getEncryptedWritableDb(PASSWORD);
    }

    public long insertGroup(Group group) {
        return groupDao.insert(group);
    }

    public List<Group> findAllGroup() {
        return groupDao.queryBuilder().list();
    }

    public void deleteGroupById(long groupId) {
        groupDao.deleteByKey(groupId);
    }

    public Group findGroupById(long groupId) {
        return groupDao.queryBuilder().where(GroupDao.Properties.Id.eq(groupId)).build().unique();
    }

    public Item findItemById(long itemId) {
        return itemDao.queryBuilder().where(ItemDao.Properties.Id.eq(itemId)).build().unique();
    }

    public void delete(Item item){
        itemDao.delete(item);
    }

    public void deleteItems(List<Item> items) {
        itemDao.deleteInTx(items);
    }

    public long insertItem(Item item) {
        return itemDao.insert(item);
    }

    public void updateItem(Item item){
        Item oldItem = itemDao.queryBuilder().where(ItemDao.Properties.Id.eq(item.getId())).build().unique();
        if(oldItem !=null){
            oldItem.setGroupId(item.getGroupId());
            oldItem.setTitle(item.getTitle());
            oldItem.setAccount(item.getAccount());
            oldItem.setPassword(item.getPassword());
            oldItem.setRemark(item.getRemark());
            itemDao.update(oldItem);
        }
    }
}
