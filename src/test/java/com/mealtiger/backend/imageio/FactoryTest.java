package com.mealtiger.backend.imageio;

import com.mealtiger.backend.imageio.adapters.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FactoryTest {

    @Autowired
    private ImageAdapter bitmapAdapter;

    @Autowired
    private ImageAdapter gifAdapter;

    @Autowired
    private ImageAdapter jpegAdapter;

    @Autowired
    private ImageAdapter pngAdapter;

    @Autowired
    private ImageAdapter webPAdapter;

    @Resource(name = "&bitmapAdapter")
    private ImageAdapterFactory bitmapAdapterFactory;

    @Resource(name = "&gifAdapter")
    private ImageAdapterFactory gifAdapterFactory;

    @Resource(name = "&jpegAdapter")
    private ImageAdapterFactory jpegAdapterFactory;

    @Resource(name = "&pngAdapter")
    private ImageAdapterFactory pngAdapterFactory;

    @Resource(name = "&webPAdapter")
    private ImageAdapterFactory webPAdapterFactory;


    @Test
    void objectTypeTest() {
        assertEquals(bitmapAdapterFactory.getObjectType(), BitmapAdapter.class);
        assertEquals(gifAdapterFactory.getObjectType(), GIFAdapter.class);
        assertEquals(jpegAdapterFactory.getObjectType(), JPEGAdapter.class);
        assertEquals(pngAdapterFactory.getObjectType(), PNGAdapter.class);
        assertEquals(webPAdapterFactory.getObjectType(), WebPAdapter.class);
    }

    @Test
    void objectTest() {
        assertEquals(bitmapAdapter.getClass(), BitmapAdapter.class);
        assertEquals(gifAdapter.getClass(), GIFAdapter.class);
        assertEquals(jpegAdapter.getClass(), JPEGAdapter.class);
        assertEquals(pngAdapter.getClass(), PNGAdapter.class);
        assertEquals(webPAdapter.getClass(), WebPAdapter.class);
    }

    // NEGATIVE TESTS

    @Test
    void illegalArgumentTest() {
        ImageAdapterFactory imageAdapterFactory = new ImageAdapterFactory("random");
        assertThrows(IllegalArgumentException.class, imageAdapterFactory::getObject);
        assertThrows(IllegalArgumentException.class, imageAdapterFactory::getObjectType);
    }

}
