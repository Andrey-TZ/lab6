package classes.utils;

import classes.commands.AbstractCommand;
import classes.dataBase.DBManager;
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
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class for work with collection.
 */
public class CollectionManager {
    private final String[] commandsHistory;
    private final ZonedDateTime creationDate;
    private int historyIndex = 0;
    private final Hashtable<Integer, StudyGroup> groups = new Hashtable<Integer, StudyGroup>();
    private String login = "";
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    /**
     * Constructor. Creates the object to work with collection.
     */
    public CollectionManager() throws SQLException {
        getCollectionFromDB();
        this.commandsHistory = new String[15];
        this.creationDate = ZonedDateTime.now();
    }

    private void block() {
        lock.readLock().lock();
        lock.writeLock().lock();
    }


    private void unlock() {
        lock.readLock().unlock();
        lock.writeLock().unlock();
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private void getCollectionFromDB() throws SQLException {
        block();
        try {
            ResultSet studyGroupsDB = DBManager.executeQuery("SELECT * FROM study_groups");
            if (studyGroupsDB == null) this.clear();
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
        unlock();

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

    public String update(int id, StudyGroup group) {
        block();
        StudyGroup group1 = getById(id);
        if (group1 == null) {
            return "Элемент с id = " + id + " не найден";
        }
        if (login.equals(group1.getUser())) {
            Integer admin_id = updateAdmin(group1, group);
            int form = group.getFormOfEducation().getId();
            Integer semester;
            if (group.getSemesterEnum() == null) semester = null;
            else semester = group.getSemesterEnum().getId();
            Coordinates coordinates = group.getCoordinates();
            if (DBManager.updateStudyGroup(id, group.getName(), coordinates.getX(), coordinates.getY(), Date.valueOf(group.getCreationDate().toLocalDate()), group.getStudentsCount(), form, semester, admin_id)) {
                group1.update(group);
                unlock();
                return "Элемент успешно обновлён";

            } else {
                unlock();
                return "Не получилось обновить элемент";
            }
        }
        unlock();
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
    public String insert(Integer key, StudyGroup group) throws WrongArgumentException {
        if (isKeyExist(key)) return "Элемент с таким ключом уже существует!";
        block();
        Integer admin = null;
        Integer semester = null;
        if (group.getSemesterEnum() != null) semester = group.getSemesterEnum().getId();
        if (group.getGroupAdmin() != null) {
            admin = DBManager.insertAdmin(group.getGroupAdmin());
        }
        int id = DBManager.insertStudyGroup(key, group.getName(), group.getCoordinates().getX(), group.getCoordinates().getY(), Date.valueOf(group.getCreationDate().toLocalDate()), group.getStudentsCount(), group.getFormOfEducation().getId(), semester, admin, login);
        if (id != 0) {
            group.setId(id);
            groups.put(key, group);
            unlock();
            return "Элемент успешно добавлен";
        }
        unlock();
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
    public String removeByKey(Integer key) {
        block();
        if (groups.containsKey(key)) {
            if (login.equals(groups.get(key).getUser())) {
                DBManager.removeStudyGroup(groups.get(key).getId());
                groups.remove(key);
                unlock();
                return "Элемент удалён";
            }
            unlock();
            return "Этот элемент добавил другой пользователь";
        } else{
            unlock();
            return ("Элемент с таким ключом не найден!");
        }
    }

    /**
     * Delete all elements from collection
     */
    public void clear() {
        groups.clear();
    }


    /**
     * Remove less elements
     *
     * @param group element to compare
     */
    public int removeLower(StudyGroup group) {
        block();
        int score = 0;
        for (Integer key : groups.keySet()) {
            if (groups.get(key).compareTo(group) < 0 && login.equals(groups.get(key).getUser())) {
                if (DBManager.removeStudyGroup(groups.get(key).getId())) {
                    groups.remove(key);
                }
            }
        }
        unlock();
        return score;

    }

    /**
     * Remove elements with less key
     *
     * @param key key to compare
     * @return numbers of deleted elements
     */
    public int removeLowerKey(Integer key) {
        block();
        ArrayList<Integer> deleted = new ArrayList<>();
        for (Integer key_i : groups.keySet()) {
            if (key_i < key && Objects.equals(this.login, groups.get(key_i).getUser())) deleted.add(key_i);
        }
        for (Integer key_i : deleted) groups.remove(key_i);
        unlock();
        return DBManager.removeLowerKeys(key, this.login);
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
    public void addToHistory(AbstractCommand command) {
        if (historyIndex < 15) {
            commandsHistory[historyIndex++] = command.getName();
        } else {
            for (int i = 0; i < 14; i++) {
                commandsHistory[i] = commandsHistory[i + 1];
            }
            commandsHistory[14] = command.getName();
        }
    }

    /**
     * Getting history of the last commands
     *
     * @return history of the last commands
     */
    public String[] getHistory() {
        return commandsHistory;
    }

}
