
## New module dependencies
* Note from build.gradle:
//core depends on libgdx_utils and obtains gdx from this dependency. Why am I commenting the obvious?
// because between ea4f425bacec762db581b621ed5fd6bcd3de712d and b3dcf30888ba33322ee4817dcc8649eeaa4f1c3a
// this was added but Gradle was not recognising utils GameBase Gradle was updated and eventually
// saw this class but only when the dependencies were also added explicitly to the these modules 
// own build.gradles. At one point the dependency also had to be added to Desktop. Bare this in mind
// if using this as start template for project.