package classes.utils;

import classes.commands.AbstractCommand;
import classes.dataBase.DBManager;
import classes.dataBase.UserData;
import classes.model.*;
import exceptions.EmptyFieldException;
import exceptions.WrongArgumentException;
import exceptions.WrongFieldException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class for work with collection.
 */
public class CollectionManager {
    private final HashMap<String, ArrayList<String>> history = new HashMap<>();
    private final ZonedDateTime creationDate;
    private final Hashtable<Integer, StudyGroup> groups = new Hashtable<Integer, StudyGroup>();
    private final ReentrantReadWriteLock locker = new ReentrantReadWriteLock(true);
    private final Lock readLock = locker.readLock();
    private final Lock writeLock = locker.writeLock();


    /**
     * Constructor. Creates the object to work with collection.
     */
    public CollectionManager() throws SQLException {
        getCollectionFromDB();
        this.creationDate = ZonedDateTime.now();
    }


    private void getCollectionFromDB() throws SQLException {

        try {
            ResultSet studyGroupsDB = DBManager.executeQuery("SELECT * FROM study_groups");
            if (studyGroupsDB == null) groups.clear();
            else {
                while (studyGroupsDB.next()) {
                    Person adminGroup = null;

                    if (studyGroupsDB.getObject("admin_id") != null) {
                        ResultSet admin = DBManager.getAdmin(studyGroupsDB.getInt("admin_id"));
                        admin.next();
                        adminGroup = new Person(admin.getInt(1), admin.getString(2), admin.getDate(3), admin.getFloat(4));
                        admin.close();
                    }


                    Coordinates coordinates = new Coordinates(studyGroupsDB.getFloat("coordinateX"), studyGroupsDB.getFloat("coordinateY"));
                    ResultSet res_form = DBManager.getFormOfEducation(studyGroupsDB.getInt("form_of_education_id"));
                    res_form.next();
                    FormOfEducation form = FormOfEducation.valueOf(res_form.getString(2));
                    res_form.close();
                    Semester semester = null;
                    if (studyGroupsDB.getObject("semester_id") != null) {
                        ResultSet res_sem = DBManager.getSemester(studyGroupsDB.getInt("semester_id"));
                        res_sem.next();
                        semester = Semester.valueOf(res_sem.getString(2));
                        res_sem.close();
                    }
                    StudyGroup group = new StudyGroup(studyGroupsDB.getInt("group_id"), studyGroupsDB.getString("name"), coordinates, (studyGroupsDB.getTimestamp("creation_date").toLocalDateTime()), studyGroupsDB.getLong("students_count"), form, semester, adminGroup, studyGroupsDB.getString("login"));
                    this.groups.put(studyGroupsDB.getInt("key"), group);
                }
                studyGroupsDB.close();
            }
        } catch (WrongFieldException e) {
            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
        } catch (EmptyFieldException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Get info about collection
     *
     * @return string with info
     */
    public String info() {
        return "Информация о коллекции:\n" + "Инициализировано: " + this.creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "\nТип коллекции: " + this.groups.getClass().getName() + "\nКоличество элементов: " + groups.size();
    }

    /**
     * Checks if there are elements in the collection
     *
     * @return true if collection is empty
     */
    public boolean isEmpty() {
        return groups.isEmpty();
    }

    /**
     * Getting element by its id
     *
     * @param id of element
     * @return element of collection by its id
     */
    public StudyGroup getById(int id) {
        for (StudyGroup group : groups.values()) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    public String update(int id, StudyGroup group, UserData user) {
        StudyGroup group1 = getById(id);
        if (group1 == null) {
            return "Элемент с id = " + id + " не найден";
        }
        if (user.getLogin().equals(group1.getUser())) {
            Integer admin_id = updateAdmin(group1, group);
            int form = group.getFormOfEducation().getId();
            Integer semester;
            if (group.getSemesterEnum() == null) semester = null;
            else semester = group.getSemesterEnum().getId();
            Coordinates coordinates = group.getCoordinates();
            if (DBManager.updateStudyGroup(id, group.getName(), coordinates.getX(), coordinates.getY(), Date.valueOf(group.getCreationDate().toLocalDate()), group.getStudentsCount(), form, semester, admin_id)) {
                group1.update(group);
                return "Элемент успешно обновлён";

            } else {
                return "Не получилось обновить элемент";
            }
        }
        return "Это элемент был добавлен другим пользователем - вы не можете его изменить!";

    }

    public Integer updateAdmin(StudyGroup oldGroup, StudyGroup newGroup) {
        Integer admin_id;
        Person admin = newGroup.getGroupAdmin();
        Person admin1 = oldGroup.getGroupAdmin();
        if (!Objects.equals(oldGroup.getGroupAdmin(), newGroup.getGroupAdmin())) {
            if (newGroup.getGroupAdmin() == null) admin_id = null;
            else if (oldGroup.getGroupAdmin() == null) {
                admin_id = DBManager.insertAdmin(newGroup.getGroupAdmin());
            } else {
                DBManager.updAdmin(admin1.getId(), admin);
                admin_id = admin1.getId();
            }
        } else if (oldGroup.getGroupAdmin() == null) admin_id = null;
        else admin_id = admin1.getId();
        return admin_id;
    }

    /**
     * Print all elements from collection
     */
    public Collection<StudyGroup> show() {
        return groups.values();
    }

    /**
     * insert ne element by its key
     *
     * @param key of new element in collection
     * @throws WrongArgumentException if element with this key already exists
     */
    public String insert(Integer key, StudyGroup group, UserData user) throws WrongArgumentException {
        if (isKeyExist(key)) return "Элемент с таким ключом уже существует!";

        Integer admin = null;
        Integer semester = null;
        if (group.getSemesterEnum() != null) semester = group.getSemesterEnum().getId();
        if (group.getGroupAdmin() != null) {
            admin = DBManager.insertAdmin(group.getGroupAdmin());
        }
        int id = DBManager.insertStudyGroup(key, group.getName(), group.getCoordinates().getX(), group.getCoordinates().getY(), Date.valueOf(group.getCreationDate().toLocalDate()), group.getStudentsCount(), group.getFormOfEducation().getId(), semester, admin, user.getLogin());
        if (id != 0) {
            group.setId(id);
            group.setUser(user.getLogin());
            groups.put(key, group);
            return "Элемент успешно добавлен";
        }
        return "Не удалось добавить элемент";
    }

    /**
     * Checking is the element with this key exists
     *
     * @param key of element in collection
     * @return true if element with this key exist
     */
    public boolean isKeyExist(Integer key) {
        return groups.containsKey(key);
    }


    /**
     * Remove element from collection by its key
     *
     * @param key of element
     */
    public String removeByKey(Integer key, UserData user) {
        if (groups.containsKey(key)) {
            if (user.getLogin().equals(groups.get(key).getUser())) {
                writeLock.lock();
                DBManager.removeStudyGroup(groups.get(key).getId());
                groups.remove(key);
                writeLock.unlock();
                return "Элемент удалён";
            }
            return "Этот элемент добавил другой пользователь";
        } else {
            return ("Элемент с таким ключом не найден!");
        }
    }

    /**
     * Delete all elements from collection for currentUser
     */
    public String clear(UserData user) {
        String login = user.getLogin();
        Integer count = DBManager.clear(login);

        if (count == 0) {
            return "Ни один элемент не удален";
        }
        Set<Integer> keys = groups.keySet();
        for (Integer key : keys
        ) {
            if(groups.get(key).getUser().equals(login)){
                groups.remove(key);
            }
        }
        return "Удалено элементов " + count;
    }


    /**
     * Remove less elements
     *
     * @param group element to compare
     */
    public int removeLower(StudyGroup group, UserData user) {
        int score = 0;
        Set<Integer> keys = groups.keySet();
        for (Integer key : keys) {
            if (groups.get(key).compareTo(group) < 0 && user.getLogin().equals(groups.get(key).getUser())) {
                if (DBManager.removeStudyGroup(groups.get(key).getId()) == 1) {
                    groups.remove(key);
                }
            }
        }
        return score;

    }

    /**
     * Remove elements with less key
     *
     * @param key key to compare
     * @return numbers of deleted elements
     */
    public int removeLowerKey(Integer key, UserData user) {
        ArrayList<Integer> deleted = new ArrayList<>();
        for (Integer key_i : groups.keySet()) {
            if (key_i < key && Objects.equals(user.getLogin(), groups.get(key_i).getUser())) deleted.add(key_i);
        }
        for (Integer key_i : deleted) groups.remove(key_i);
        return DBManager.removeLowerKeys(key, user.getLogin());
    }

    /**
     * Finding elements which name starting from this substring
     *
     * @param name getting string for filter
     * @return elements witch starting from this substring
     */
    public Set<StudyGroup> filterStartsWithName(String name) {
        Set<StudyGroup> groupStartsWithName = new LinkedHashSet<>();
        for (StudyGroup group : groups.values()) {
            if (group.getName().startsWith(name)) {
                groupStartsWithName.add(group);
            }
        }
        return groupStartsWithName;
    }

    /**
     * Finding elements which groupAdmin is less
     *
     * @param groupAdmin element to compare
     * @return elements witch groupAdmin is less
     */
    public Set<StudyGroup> filterLessThanGroupAdmin(Person groupAdmin) {
        Set<StudyGroup> groupsWithLessAdmin = new LinkedHashSet<>();
        if (groupAdmin == null) return null;
        for (StudyGroup group : groups.values()) {
            if (group.getGroupAdmin() == null) groupsWithLessAdmin.add(group);
            else if (group.getGroupAdmin().compareTo(groupAdmin) < 0) {
                groupsWithLessAdmin.add(group);
            }
        }
        return groupsWithLessAdmin;
    }

    /**
     * finding all unique values of studentsCount
     *
     * @return unique values of studentsCount
     */
    public Set<Long> getUniqueStudentsCount() {
        Set<Long> uniq = new LinkedHashSet<>();
        for (StudyGroup group : groups.values()) {
            uniq.add(group.getStudentsCount());
        }
        return uniq;
    }

    /**
     * Add command to history
     *
     * @param command command which will be added to history
     */
    public void addToHistory(AbstractCommand command, UserData user) {
        String login = user.getLogin();
        this.writeLock.lock();
        try {
            if (!history.containsKey(login)) {
                ArrayList<String> historyForUser = new ArrayList<String>(15);
                historyForUser.add(command.getName());
                history.put(login, historyForUser);
            } else {
                ArrayList<String> historyForCurrentUser = history.get(login);
                if (historyForCurrentUser.size() >= 15) {
                    historyForCurrentUser.remove(0);
                }
                historyForCurrentUser.add(command.getName());
            }
        } finally {
            this.writeLock.unlock();
        }

    }

    /**
     * Getting history of the last commands
     *
     * @return history of the last commands
     */
    public ArrayList<String> getHistory(UserData user) {
        ArrayList<String> historyForCurrentUser;
        this.readLock.lock();
        try {
            historyForCurrentUser = history.get(user.getLogin());
        } finally {
            this.readLock.unlock();
        }
        return historyForCurrentUser;
    }

}
