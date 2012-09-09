require 'brazenhead/builder'
require 'brazenhead/device'

module Brazenhead
  class Server
    include Brazenhead::Package
    include ADB

    def initialize(apk)
      @apk = apk
    end

    def start(activity)
      build
      instrument(runner, :packageName => their_package, :fullLauncherName => full(activity) , :class => the_test)
    end

    def stop
      device.stop
    end

    private
    def build
      @manifest_info ||= Brazenhead::Builder.new.build_for(@apk)
    end

    def device
      @device ||= Brazenhead::Device.new
    end

    def the_test
      "#{leandog}.TheTest"
    end

    def full(activity)
      "#{their_package}.#{activity}"
    end

    def their_package
      @manifest_info.package
    end

    def runner
      "#{leandog}/#{leandog}.BrazenheadInstrumentation"
    end

    def leandog
      'com.leandog.brazenhead'
    end

  end
end