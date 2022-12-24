package com.mealtiger.backend.imageio.adapters;

import java.io.IOException;

public interface ImageAdapter {

    byte[] convert() throws IllegalStateException, IllegalArgumentException, IOException;

}
