# Gametel
[![Build Status](https://secure.travis-ci.org/leandog/gametel-driver.png?branch=master)](http://travis-ci.org/leandog/gametel-driver)

A low-level driver for testing android applications.

## Basic Usage

Gametel-driver works by modifying your Android application (apk).  Once modified and installed in an emulator or device, gametel-driver can send request to the emulator or device and cause it to interact with your application via the [Robotium API](http://code.google.com/p/robotium/).

Messages sent to the emulator are json messages and  must take the form of 

````ruby
{ 
  name: 'theMethodName',  # The Robotium method to call
  arguments: [1, 2, 3],   # The arguments to be passed to the Robotium method
  variable: "@@variable_name_to_store_the_results@@"  # optional parameter to store the results 
                                                      # of a call - can be used in subsequent calls
  target:  'Robotium'     # optional parameter.  Valid values are 'Robotium' or 'LastResultOrRobotium'
}
````

The `variable` parameter can be used in subsequent calls as an argument value.

In addition to a server process there is a ruby module that will build the json for you when you call corresponding methods.  Let's take a look at a few examples of how this works.

You can include the `GametelDriver` module in your class to provide the abilities.

````Ruby
class MyClass
  include GametelDriver
end
````

Once you do this you can call methods on an instance of `MyClass`.  These methods look like this:

````Ruby
my_class.scroll_down  # call the scrollDown method on Robotium
````

````Ruby
my_class.scroll_down
my_class.scroll_up {:target => 'Robotium'}  # needed to provide target.  Otherwise it would
                                            # default to 'LastResultOrRobotium' and call
                                            # scrollUp on the boolean returned by the call
                                            # to scrollDown
````



## Known Issues

See [http://github.com/leandog/gametel-driver/issues](http://github.com/leandog/gametel-driver/issues)

## Contribute
 
* Fork the project.
* Test drive your feature addition or bug fix. Adding specs is important and I will not accept a pull request that does not have tests.
* Make sure you describe your new feature with a cucumber scenario.
* Make sure you provide RDoc comments for any new public method you add. Remember, others will be using this gem.
* Commit, do not mess with Rakefile, version, or ChangeLog.
  (if you want to have your own version, that is fine but bump version in a commit by itself I can ignore when I pull)
* Send me a pull request. Bonus points for topic branches.

## Copyright

Copyright (c) 2012 Jeffrey S. Morgan, Levi Wilson. See LICENSE for details.

