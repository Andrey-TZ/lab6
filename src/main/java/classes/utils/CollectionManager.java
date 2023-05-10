package classes.utils;

import classes.commands.AbstractCommand;
import exceptions.WrongArgumentException;
import classes.jsonParser.JsonParser;
import classes.model.Person;
import classes.model.StudyGroup;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class for work with collection.
 */
public class CollectionManager {
    private final String[] commandsHistory;
    private ZonedDateTime creationDate;
    private int historyIndex = 0;
    private final Hashtable<Integer, StudyGroup> groups;
    private final JsonParser parser;

    /**
     * Constructor. Creates the object to work with collection.
     */
    public CollectionManager(String[] args) {
        Hashtable<Integer, StudyGroup> groups1;
        parser = new JsonParser();
        groups1 = parser.collectionFromJson(args);
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
        String result = "Информация о коллекции:\n";
        result += "Инициализировано: " + this.creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        result += "\nТип коллекции: " + this.groups.getClass().getName();
        result += "\nКоличество элементов: " + groups.size();
        return result;
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

    /**
     * Print all elements from collection
     */
    public void show() {
        if (groups.isEmpty()) System.out.println("В коллекции пока нет ни одного элемента");
        else {
            for (StudyGroup group : groups.values()) {
                System.out.println(group);
            }
        }
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
        System.out.println("Коллекция успешно очищена!");
    }

    /**
     * Save collection to file
     */
    public void save() {
        parser.writeJson(groups);
        System.out.println("Коллекция успешно сохранена!");
    }


    /**
     * Remove less elements
     *
     * @param group element to compare
     */
    public void removeLower(StudyGroup group) {
        ArrayList<Integer> deleted_groups = new ArrayList<>();
        for (Integer key : groups.keySet()) {
            if (groups.get(key).compareTo(group) < 0) deleted_groups.add(key);

        }
        for (Integer key : deleted_groups) groups.remove(key);

        System.out.println("Удалено элементов: " + deleted_groups.size());

    }

    /**
     * Remove elements with less key
     *
     * @param key key to compare
     */
    public void removeLowerKey(Integer key) {
        ArrayList<Integer> deleted = new ArrayList<>();
        for (Integer key_i : groups.keySet()) {
            if (key_i < key) deleted.add(key_i);
        }
        for (Integer key_i : deleted) groups.remove(key_i);

        if (!deleted.isEmpty()) System.out.println("Успешно удалено элементов: " + deleted.size());
        else System.out.println("Не удалось найти ни одного элемента по таким параметрам");
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
