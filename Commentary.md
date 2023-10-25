
## New module dependencies
* Note regarding build.gradle dependencies and sourceCompatibility 1.8:
- There are a lot of commented out dependencies within the individual gradle build files. This is
    because after debugging it was interesting to see how pared back the additions could be. It seems
    that a lot of the issues were caused by the java language level target being 1.7 and not 1.8.
    As ever, the additions were effective at one point or another along the way. They are left for reference.