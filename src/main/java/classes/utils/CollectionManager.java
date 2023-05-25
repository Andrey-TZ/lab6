package classes.utils;

import classes.commands.AbstractCommand;
import classes.model.Person;
import classes.model.StudyGroup;
import classes.shells.Response;
import exceptions.WrongArgumentException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class for work with collection.
 */
public class CollectionManager {
    private final String[] commandsHistory;
    private final ZonedDateTime creationDate;
    private int historyIndex = 0;
    private final Hashtable<Integer, StudyGroup> groups;
    private Response response = new Response();

    /**
     * Constructor. Creates the object to work with collection.
     */
    public CollectionManager() {
        Hashtable<Integer, StudyGroup> groups1;
        groups1 = FIleManager.readJson();
        if (groups1 == null) {
            groups1 = new Hashtable<Integer, StudyGroup>();
        }
        this.groups = groups1;
        this.commandsHistory = new String[15];
        this.creationDate = ZonedDateTime.now();
    }

    /**
     * Get info about collection
     *
     * @return string with info
     */
    public String info() {
        return "Информация о коллекции:\n" +
                "Инициализировано: " + this.creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                + "\nТип коллекции: " + this.groups.getClass().getName()
                + "\nКоличество элементов: " + groups.size();
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

    public boolean isExistById(int id) {
        for (StudyGroup group : groups.values()) {
            if (group.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public String update(int id, StudyGroup group) {
        StudyGroup group1 = getById(id);
        if (group1 == null) {
            return "Элемент с id = " + id + "не найден";
        }
        group.setId(group1.getId());
        group1 = group;

        return "Элемент успешно обновлён";
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
    public void insert(Integer key, StudyGroup group) throws WrongArgumentException {
        if (isKeyExist(key)) throw new WrongArgumentException("Элемент с таким ключом уже существует!");
        groups.put(key, group);

    }

    /**
     * Checking is element with this key exist
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
    public void removeByKey(Integer key) {
        if (groups.containsKey(key)) {
            groups.remove(key);
        } else System.out.println("Элемент с таким ключом не найден!");
    }

    /**
     * Delete all elements from collection
     */
    public void clear() {
        groups.clear();
    }

    /**
     * Save collection to file
     */
    public Response save() {
        FIleManager.writeJson(groups);
        response.setData("Коллекция успешно сохранена!");
        return response;
    }


    /**
     * Remove less elements
     *
     * @param group element to compare
     */
    public int removeLower(StudyGroup group) {
        ArrayList<Integer> deleted_groups = new ArrayList<>();
        for (Integer key : groups.keySet()) {
            if (groups.get(key).compareTo(group) < 0) deleted_groups.add(key);

        }
        for (Integer key : deleted_groups) groups.remove(key);
        return deleted_groups.size();

    }

    /**
     * Remove elements with less key
     *
     * @param key key to compare
     * @return
     */
    public int removeLowerKey(Integer key) {
        ArrayList<Integer> deleted = new ArrayList<>();
        for (Integer key_i : groups.keySet()) {
            if (key_i < key) deleted.add(key_i);
        }
        for (Integer key_i : deleted) groups.remove(key_i);

        return deleted.size();
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
