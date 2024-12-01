package org.dmx.gd32;

public class PixelConfiguration {
	static final String PIXEL_TYPES[] = {"WS2801", "WS2811", "WS2812", "WS2812B", "WS2813", "WS2815", "SK6812", "SK6812W", "UCS1903", "UCS2903", "CS8812", "APA102", "SK9822", "P9813"};
	private final int PIXEL_SPI[] = {0, 11, 12, 13};
	private final int MAX_LED_COUNT_RGBW = (4 * 128);
	private final int MAX_LED_COUNT_RGB = (4 * 170);
	private String pixelType;
	private int count;
	private Boolean isSupported;
	private Boolean isRTZ;
	private int ledsPerPixel = 3;
	private int clockSpeedHz;
	private int refreshRate;

	public static void main(String[] args) {
		PixelConfiguration pixelConfiguration = new PixelConfiguration("CS8812", 8);
		if (pixelConfiguration.isSupported()) {
			System.out.println("Ok");
		}
		if (pixelConfiguration.isRTZ()) {
			System.out.println("RTZ");
		}

	}
	
	public PixelConfiguration(String pixelType, int count, int clockSpeedHz) {
		this.pixelType = pixelType.toUpperCase();
		this.count = count;
		this.clockSpeedHz = clockSpeedHz;
		validate();
	}

	public PixelConfiguration(String pixelType, int count) {
		this.pixelType = pixelType.toUpperCase();
		this.count = count;
		this.clockSpeedHz = 6400000;
		validate();
	}
	
	public Boolean isSupported() {
		return isSupported;
	}
	
	public Boolean isRTZ() {
		return isRTZ;
	}
	
	public int getLedsPerPixel()  {
		return ledsPerPixel;
	}
	
	public int getRefreshRate()  {
		return refreshRate;
	}
	
	public int getCount()  {
		return count;
	}
	
	private void validate() {
		isSupported = false;
		for (int i = 0; i < PIXEL_TYPES.length; i++) {
			if (this.pixelType.equals(PIXEL_TYPES[i])) {
				isSupported = true;
				break;
			}
		}

		isRTZ = true;
		for (int i = 0; i < PIXEL_SPI.length; i++) {
			if (this.pixelType.equals(PIXEL_TYPES[PIXEL_SPI[i]])) {
				isRTZ = false;
				break;
			}
		}

		if (isRTZ) {
			clockSpeedHz = 6400000;
		}

		if (this.pixelType.equals(PIXEL_TYPES[7])) {
			count = count > MAX_LED_COUNT_RGBW ? MAX_LED_COUNT_RGBW : count;
			ledsPerPixel = 4;
		} else {
			count = count > MAX_LED_COUNT_RGB ? MAX_LED_COUNT_RGB : count;
			ledsPerPixel = 3;
		}

		if (isRTZ) {
			//                  8 * 1000.000
			// led time (us) =  ------------ * 8 = 10 us
			//                   6.400.000
			final int ledsTime = 10 * count * ledsPerPixel;
			refreshRate = 1000000 / ledsTime;
		} else {
			final int ledTime = (8 * 1000000) / clockSpeedHz;
			final int ledsTime = ledTime * count * ledsPerPixel;
			refreshRate = 1000000 / ledsTime;			
		}
	}
}
