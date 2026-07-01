/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.srgutils;

import java.io.File;
import java.io.IOException;

/* This is internal API, do not depend on it in code.
 * No idea why you would, but just don't.
 * This is just here because its simple, and I wanted a way to quickly convery mapping files.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File input = null;
        File output = null;
        boolean reverse = false;

        if (args.length == 2) {
            input = new File(args[0]);
            output = new File(args[1]);
        } else if (args.length == 3 && "--reverse".equals(args[0])) {
            reverse = true;
            input = new File(args[1]);
            output = new File(args[2]);
        } else {
            throw new IllegalArgumentException("Usage: java -jar srgutils.jar [--reverse] input.srg output.tsrg");
        }

        if (!input.exists())
            throw new IllegalArgumentException("Input file is missing: " + input);

        String name = output.getName();
        int idx = name.lastIndexOf('.');
        if (idx == -1)
            throw new IllegalArgumentException("Could not detect output format, no extension provided: " + name);

        String formatName = name.substring(idx + 1);
        if ("gz".equals(formatName)) {
            idx = name.lastIndexOf('.', name.length() - 3);
            if (idx == -1)
                throw new IllegalArgumentException("Could not detect output format, no extension provided: " + name);
            formatName = name.substring(idx, name.length() - 3);
        }

        IMappingFile.Format format = IMappingFile.Format.get(formatName);
        if (format == null)
            throw new IllegalArgumentException("Unknown format: " + formatName + " from  " + name);

        IMappingFile map = IMappingFile.load(input);
        File parent = output.getParentFile();
        if (parent != null && !parent.exists())
            parent.mkdirs();
        map.write(output.toPath(), format, reverse);
    }
}
