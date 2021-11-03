package com.zyx.pwdmanager.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity(indexes = {@Index(value = "groupId DESC,title DESC", unique = true)})
public class Item {
    @Id(autoincrement = true)
    Long id;

    @Index()
    Long groupId;

    @ToOne(joinProperty = "groupId")
    private Group group;

    @Index()
    String title;

    @Index()
    String account;

    @Index()
    String password;

    @Index()
    String remark;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 182764869)
    private transient ItemDao myDao;




    @Generated(hash = 1125801358)
    public Item(Long id, Long groupId, String title, String account,
            String password, String remark) {
        this.id = id;
        this.groupId = groupId;
        this.title = title;
        this.account = account;
        this.password = password;
        this.remark = remark;
    }




    @Generated(hash = 1470900980)
    public Item() {
    }




    @Generated(hash = 201187923)
    private transient Long group__resolvedKey;




    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", title='" + title + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }




    public Long getId() {
        return this.id;
    }




    public void setId(Long id) {
        this.id = id;
    }




    public Long getGroupId() {
        return this.groupId;
    }




    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }




    public String getTitle() {
        return this.title;
    }




    public void setTitle(String title) {
        this.title = title;
    }




    public String getAccount() {
        return this.account;
    }




    public void setAccount(String account) {
        this.account = account;
    }




    public String getPassword() {
        return this.password;
    }




    public void setPassword(String password) {
        this.password = password;
    }




    public String getRemark() {
        return this.remark;
    }




    public void setRemark(String remark) {
        this.remark = remark;
    }




    /** To-one relationship, resolved on first access. */
    @Generated(hash = 424827388)
    public Group getGroup() {
        Long __key = this.groupId;
        if (group__resolvedKey == null || !group__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupDao targetDao = daoSession.getGroupDao();
            Group groupNew = targetDao.load(__key);
            synchronized (this) {
                group = groupNew;
                group__resolvedKey = __key;
            }
        }
        return group;
    }




    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 421019267)
    public void setGroup(Group group) {
        synchronized (this) {
            this.group = group;
            groupId = group == null ? null : group.getId();
            group__resolvedKey = groupId;
        }
    }




    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }




    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }




    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }




    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 881068859)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getItemDao() : null;
    }



}
