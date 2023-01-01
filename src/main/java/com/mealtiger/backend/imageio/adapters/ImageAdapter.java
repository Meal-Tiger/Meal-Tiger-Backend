package com.mealtiger.backend.imageio.adapters;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageAdapter {

    byte[] convert(BufferedImage input) throws IllegalStateException, IllegalArgumentException, IOException;

}
