package de.threeseconds.collections;

import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class CollectionItemDataType implements PersistentDataType<byte[], CollectionItem> {

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<CollectionItem> getComplexType() {
        return CollectionItem.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull CollectionItem complex, @NotNull PersistentDataAdapterContext context) {
        return SerializationUtils.serialize(complex);
    }

    @Override
    public @NotNull CollectionItem fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        try {
            InputStream inputStream = new ByteArrayInputStream(primitive);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (CollectionItem) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
