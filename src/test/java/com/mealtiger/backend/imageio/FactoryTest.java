package com.mealtiger.backend.imageio;

import com.mealtiger.backend.imageio.adapters.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class tests the ImageAdapterFactory.
 */
@SpringBootTest
@Tag("unit")
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

    /**
     * Tests whether the correct ImageAdapters-Classes are returned using the getObjectType method.
     */
    @Test
    void objectTypeTest() {
        assertEquals(bitmapAdapterFactory.getObjectType(), BitmapAdapter.class);
        assertEquals(gifAdapterFactory.getObjectType(), GIFAdapter.class);
        assertEquals(jpegAdapterFactory.getObjectType(), JPEGAdapter.class);
        assertEquals(pngAdapterFactory.getObjectType(), PNGAdapter.class);
        assertEquals(webPAdapterFactory.getObjectType(), WebPAdapter.class);
    }

    /**
     * Tests whether the correct ImageAdapters are injected into the fields above.
     */
    @Test
    void objectTest() {
        assertEquals(bitmapAdapter.getClass(), BitmapAdapter.class);
        assertEquals(gifAdapter.getClass(), GIFAdapter.class);
        assertEquals(jpegAdapter.getClass(), JPEGAdapter.class);
        assertEquals(pngAdapter.getClass(), PNGAdapter.class);
        assertEquals(webPAdapter.getClass(), WebPAdapter.class);
    }

    // NEGATIVE TESTS

    /**
     * Tests whether an IllegalArgumentException is thrown when a wrong format is given.
     */
    @Test
    void illegalArgumentTest() {
        ImageAdapterFactory imageAdapterFactory = new ImageAdapterFactory("random");
        assertThrows(IllegalArgumentException.class, imageAdapterFactory::getObject);
        assertThrows(IllegalArgumentException.class, imageAdapterFactory::getObjectType);
    }

}
