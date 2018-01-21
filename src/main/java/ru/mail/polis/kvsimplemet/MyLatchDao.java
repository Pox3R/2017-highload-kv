package ru.mail.polis.kvsimplemet;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.NoSuchElementException;

public class MyLatchDao implements MyDao {
    @NotNull
    private final File dir;
    private final Map<String, byte[]> myCache;

    public MyLatchDao(@NotNull final File dir) {
        this.dir = dir;
        myCache = new HashMap<>(15000);
    }

    @NotNull
    private File getFile(@NotNull final String key) {
        return new File(dir, key);
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException {
        if (myCache.containsKey(key)) {
            return myCache.get(key);
        }
        final File file = new File(dir, key);
        if (!file.exists()) {
            myCache.put(key, null);
            throw new NoSuchElementException("Where is file?");
        }

        byte[] value = Files.readAllBytes(Paths.get(dir + File.separator + key));
        myCache.put(key, value);

        return myCache.get(key);
    }

    @Override
    public void upsert(
            @NotNull final String key,
            @NotNull final byte[] value) throws IllegalArgumentException, IOException {
        try (OutputStream os = new FileOutputStream(getFile(key))) {
            os.write(value);
        }
        myCache.remove(key);
    }

    @Override
    public void delete(@NotNull final String key) throws IllegalArgumentException, IOException {
        //noinspection ResultOfMethodCallIgnored
        getFile(key).delete();
        myCache.remove(key);
    }
}
