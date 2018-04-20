### Java Miderm w/ an Eclipse Annotation Processor

For one of my Java courses, to say I "over-engineered" my project would be an understatement.

At the time I coded this, I was particularly proud of what I did in `objects/Command.java` and `main/MENU.java` since I got to use
instanceof, anonymous functions, an enum, I read my menu from a text file, and I used interfaces out the wazoo!

However, that isn't the craziest part of the midterm that I overdid. I ended up programming an annotation processor for eclipse
to allow what I called "static inheritance". 

What is that you ask?

In my design, I thought it would be appropiate for `Movies` and `TVShows` to have static rating systems since those rating systems were
the same across all movies or all tv shows. I did not want "ratingSystem" as a member variable, since that didn't seem to really fit. My
first thought was that because a rating system is common to all forms of entertainment, I should put "ratingSystem" into my
`Entertainment` class. However, I quickly found out that there is no such thing as static inheritance.

```java
class Entertainment {

  @SubInheritsStatic
  protected static RatingSystem ratingSystem;
  
  public RatingSystem getRatingSystem() {
    try {
      return (RatingSystem) this.getClass().getField("ratingSystem").get(null);
    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
      e.printStackTrace();
      System.exit(1);
    }
    
    // This is dead code but eclipse complains without it.
    return null;
    
    /*
    //This is the safe way, but not dynamic!
    if (this instanceof Movie) {
      return Movie.ratingSystem;
    } else if (this instanceof TVShow) {
      return TVShow.ratingSystem;
    } else return null;
    */
  }
  
  ...
}

class Movie extends Entertainment {
  
  protected static RatingSystem ratingSystem;
  ...
}

class TVShow extends Entertainment {
  
  protected static RatingSystem ratingSystem;
  ...
}
```

In the above code, the ratingSystem variable of Movie and TVShow shadow the ratingSystem of the original class. The getter in the
parent class assumes that subclasses have shadowed ratingSystem, but I wanted to guarantee that all subclasses were forced to
follow this behavior. Even though I only had "Movie" and "TVShow", I wanted to make my code work supposing someone extended my
entertainment class without knowing about the static rating system.

@SubInheritsStatic is the annotation I made, which forces subclasses to shadow tagged (static) variables by making eclipse give the user
an error. I had the idea to make this annotation because of how an interface forces its subclasses to implement certain methods.

As you can see, the getter for rating system is not static, but a member variable. This was likely my number one design flaw, but I
was persistent enough to make it work!

Although my project was overall too complicated for what I was doing, I learned a lot from coding it. Throughout my project I was
conscious about design decisions, yet at the same time tried to strech the limit of my abilities.

EDIT: I would give instructions on how to load the annotation processor, but I did this so long ago I forgot. A simple google search
of "eclipse annotation processor" would probably help out. The project currently does not actually make use of the annotation processor
since it would be a pretty bad idea to rely on.


