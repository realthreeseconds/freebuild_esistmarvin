package de.threeseconds.jobs;

import de.threeseconds.collections.CollectionItem;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class JobDataType implements PersistentDataType<byte[], Job> {

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Job> getComplexType() {
        return Job.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Job complex, @NotNull PersistentDataAdapterContext context) {
        return SerializationUtils.serialize(complex);
    }

    @Override
    public @NotNull Job fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        try {
            InputStream inputStream = new ByteArrayInputStream(primitive);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (Job) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
