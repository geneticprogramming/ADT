/*
 * MIT License
 *
 * Copyright (c) 2014-2018 David Moskowitz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.infoblazer.gp.evolution.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by David on 5/31/2014.
 */

public class GpUtils {

    private static final ThreadLocal<Kryo> threadLocal =  new ThreadLocal<Kryo>();

    public static Kryo getKyroInstance(){
        Kryo kryo = threadLocal.get();
        if (kryo == null){
            kryo = new Kryo();
            kryo.addDefaultSerializer(LocalDate.class, LocalDateSerializer.class);
            threadLocal.set(kryo);
        }
        return kryo;
    }




    public static class LocalDateSerializer extends Serializer<LocalDate> {
        public void write(Kryo kryo, Output output, LocalDate ld) {

            Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            output.writeLong(date.getTime(), true);
        }

        public LocalDate read(Kryo kryo, Input input, Class<LocalDate> type) {
            Date date =new Date(input.readLong(true));
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        }

        public LocalDate copy(Kryo kryo, LocalDate original) {
            return LocalDate.from(original);
        }
    }






}
