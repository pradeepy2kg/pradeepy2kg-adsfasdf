package lk.transliterate;

/**
 * A better Transliterator for use in Sri Lanka
 *
 * Copyright (c) 2010 Asankha Perera (http://adroitlogic.org). All Rights Reserved.
 *
 * GNU Affero General Public License Usage
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program (See LICENSE-AGPL.TXT).
 * If not, see http://www.gnu.org/licenses/agpl-3.0.html
 *
 * @author asankha perera (asankha AT gmail DOT com)
 * @since 6th November 2010
 */
public class LangToPhonetic {

    private String rule;
    private Integer gender;
    private Integer length;
    private String phonetic;

    public LangToPhonetic(String rule, String gender, String length, String phonetic) {
        this.rule = rule;
        try {
            this.gender = Integer.parseInt(gender);
        } catch (NumberFormatException ignore) {}
        try {
            this.length = Integer.parseInt(length);
        } catch (NumberFormatException ignore) {}
        this.phonetic = phonetic;
    }

    public String getRule() {
        return rule;
    }

    public int getGender() {
        return gender;
    }

    public int getLength() {
        return length;
    }

    public String getPhonetic() {
        return phonetic;
    }
}
