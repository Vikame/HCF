package me.threadsafe.hcf.api;

import me.threadsafe.hcf.enumeration.Relation;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class TypeHelper<K, T> {

    private Map<K, T> bind = new HashMap<>();

    public void bind(K key, T type){
        if(key instanceof String) key = (K)((String)key).toLowerCase();

        bind.put(key, type);
    }

    public T parse(K key){
        if(key instanceof String) key = (K)((String)key).toLowerCase();

        return bind.get(key);
    }

    public String[] keys(){
        String[] array = new String[bind.size()];

        Object[] other = bind.keySet().toArray();
        for(int i = 0; i < other.length; i++) array[i] = other[i].toString();

        return array;
    }

    public static class BooleanTypeHelper extends TypeHelper<String, Boolean>{

        private static BooleanTypeHelper instance;

        private BooleanTypeHelper(){
            bind("true", true);
            bind("yes", true);
            bind("false", false);
            bind("no", false);
            bind("1", true);
            bind("0", false);

            instance = this;
        }

        public static BooleanTypeHelper getInstance(){
            return (instance == null ? new BooleanTypeHelper() : instance);
        }

    }

    public static class EnvironmentTypeHelper extends TypeHelper<World.Environment, String>{

        private static EnvironmentTypeHelper instance;

        private EnvironmentTypeHelper(){
            bind(World.Environment.NORMAL, "Overworld");
            bind(World.Environment.NETHER, "The Nether");
            bind(World.Environment.THE_END, "The End");

            instance = this;
        }

        public static EnvironmentTypeHelper getInstance(){
            return (instance == null ? new EnvironmentTypeHelper() : instance);
        }

    }

    public static class RelationTypeHelper extends TypeHelper<String, Relation>{

        private static RelationTypeHelper instance;

        private RelationTypeHelper(){
            bind("neutral", Relation.NEUTRAL);
            bind("enemy", Relation.ENEMY);
            bind("ally", Relation.ALLY);
            bind("member", Relation.MEMBER);

            instance = this;
        }

        public static RelationTypeHelper getInstance(){
            return (instance == null ? new RelationTypeHelper() : instance);
        }

    }

}
