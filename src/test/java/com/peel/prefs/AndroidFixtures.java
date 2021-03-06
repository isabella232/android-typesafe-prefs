/*
 * Copyright (C) 2018 Peel Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peel.prefs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utility class to provide mocks for various Android library classes
 *
 * @author Inderjeet Singh
 */
public class AndroidFixtures {
    public static class PrefsListener {
        public void onGet(String key) {}
        public void onPut(String key, Object value) {}
        public void onRemove(String key) {}
        public void onInit(SharedPreferences prefs) {}
    }

    public static Context createMockContext() {
        return createMockContext(null);
    }

    public static Context createMockContext(PrefsListener listener, String... prefsFileNames) {
        Context context = Mockito.mock(Context.class);
        PowerMockito.mockStatic(PreferenceManager.class);
        SharedPreferences defaultPrefs = createMockSharedPreferences(context, listener);
        Mockito.when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(defaultPrefs);
        if (listener != null) listener.onInit(defaultPrefs);
        if (prefsFileNames != null) {
            for (String prefsFileName : prefsFileNames) {
                SharedPreferences prefs = createMockSharedPreferences(context, listener);
                Mockito.when(context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)).thenReturn(prefs);
                if (listener != null) listener.onInit(prefs);
            }
        }
        return context;
    }

    public static SharedPreferences createMockSharedPreferences(Context context, final PrefsListener listener) {
        final Map<String, Object> map = new HashMap<>();
        return new SharedPreferences() {
            @Override public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
            @Override public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
            @SuppressWarnings("unchecked") private <T> T get(String key, T defValue) {
                if (listener != null) listener.onGet(key);
                return map.containsKey(key) ? (T) map.get(key) : defValue;
            }
            @Override public Set<String> getStringSet(String key, Set<String> defValues) {
                if (listener != null) listener.onGet(key);
                return get(key, defValues);
            }
            @Override public String getString(String key, String defValue) {
                if (listener != null) listener.onGet(key);
                return get(key, defValue);
            }
            @Override public long getLong(String key, long defValue) {
                if (listener != null) listener.onGet(key);
                return get(key, defValue);
            }
            @Override public int getInt(String key, int defValue) {
                if (listener != null) listener.onGet(key);
                return get(key, defValue);
            }
            @Override public float getFloat(String key, float defValue) {
                if (listener != null) listener.onGet(key);
                return get(key, defValue);
            }
            @Override public boolean getBoolean(String key, boolean defValue) {
                if (listener != null) listener.onGet(key);
                return get(key, defValue);
            }
            @Override public Map<String, ?> getAll() {
                return map;
            }
            @Override public boolean contains(String key) {
                if (listener != null) listener.onGet(key);
                return map.containsKey(key);
            }
            @Override public Editor edit() {
                return new Editor() {
                    @Override public Editor remove(String key) {
                        map.remove(key);
                        if (listener != null) listener.onRemove(key);
                        return this;
                    }
                    @Override public Editor putStringSet(String key, Set<String> values) {
                        map.put(key, values);
                        if (listener != null) listener.onPut(key, values);
                        return this;
                    }
                    @Override public Editor putString(String key, String value) {
                        map.put(key, value);
                        if (listener != null) listener.onPut(key, value);
                        return this;
                    }
                    @Override public Editor putLong(String key, long value) {
                        map.put(key, value);
                        if (listener != null) listener.onPut(key, value);
                        return this;
                    }
                    @Override public Editor putInt(String key, int value) {
                        map.put(key, value);
                        if (listener != null) listener.onPut(key, value);
                        return this;
                    }
                    @Override public Editor putFloat(String key, float value) {
                        map.put(key, value);
                        if (listener != null) listener.onPut(key, value);
                        return this;
                    }
                    @Override public Editor putBoolean(String key, boolean value) {
                        map.put(key, value);
                        if (listener != null) listener.onPut(key, value);
                        return this;
                    }
                    @Override public boolean commit() {
                        return true;
                    }
                    @Override public Editor clear() {
                        map.clear();
                        return this;
                    }
                    @Override public void apply() {} // no op
                };
            }
        };
    }

    private AndroidFixtures() {} // Not instantiable
}
