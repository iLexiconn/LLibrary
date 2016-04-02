package net.ilexiconn.llibrary.client.lang;

public class RemoteLanguageContainer {
    public LangContainer[] languages;

    public class LangContainer {
        public String locale;
        public String downloadURL;
    }
}
