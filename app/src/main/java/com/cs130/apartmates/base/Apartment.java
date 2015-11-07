package com.cs130.apartmates.base;

import java.util.ArrayList;
import java.util.Iterator;

import com.cs130.apartmates.base.users.User;

/**
 * Created by Eric Du on 10/26/2015.
 *
 * Right now we have two sets of members, one held as a list of User
 * objects and another held as a set of user IDs in the RotationTaskManager.
 * May be able to combine these two in some way.
 *
 */
public class Apartment {
    private ArrayList<User> m_members;
    private RotationTaskManager m_manager;
    private long m_apartment_id;

    public Apartment(long apartment_id) {
        m_members = new ArrayList<User>();
        m_manager = new RotationTaskManager();
        m_apartment_id = apartment_id;
    }

    public ArrayList<User> getMembers() {
        return m_members;
    }

    public void addUser(User user) {
        m_members.add(user);
        m_manager.addMember(user.getId());
    }

    public void removeUser(long id) {
        Iterator<User> iterator =  m_members.iterator();
        while(iterator.hasNext()) {
            User user = iterator.next();
            if(user.getId() == id) {
                iterator.remove();
                m_manager.removeMember(id);
            }
        }
    }

    public RotationTaskManager getManager() {
        return m_manager;
    }

    public long getApartmentId() {
        return m_apartment_id;
    }

    public void setApartmentId(long m_apartment_id) {
        this.m_apartment_id = m_apartment_id;
    }
}
