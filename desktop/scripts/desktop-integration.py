#!/usr/bin/env python
from dbus.mainloop.glib import DBusGMainLoop
import appindicator
import dbus
import dbus
import dbus.service
import gtk
import gtk
import imaplib
import re
import sys

class DesktopIntegration(dbus.service.Object):
    def __init__(self):
        self.bus_name = dbus.service.BusName('net.mms_projects.copyit.DesktopIntegration', bus=dbus.SessionBus())
        dbus.service.Object.__init__(self, self.bus_name, '/')

    def menu_setup(self):
        self.menu = gtk.Menu()

        self.menu_item_push = gtk.MenuItem("Copy It")
        self.menu_item_push.connect("activate", self.menu_action_push)
        self.menu_item_push.show()
        self.menu.append(self.menu_item_push)
        
        self.menu_item_pull = gtk.MenuItem("Paste It")
        self.menu_item_pull.connect("activate", self.menu_action_pull)
        self.menu_item_pull.show()
        self.menu.append(self.menu_item_pull)
        
        self.menu_item_open_preferences = gtk.MenuItem("Preferences")
        self.menu_item_open_preferences.connect("activate", self.menu_action_open_preferences)
        self.menu_item_open_preferences.show()
        self.menu.append(self.menu_item_open_preferences)
        
        self.menu_item_open_about = gtk.MenuItem("About")
        self.menu_item_open_about.connect("activate", self.menu_action_open_about)
        self.menu_item_open_about.show()
        self.menu.append(self.menu_item_open_about)

        self.quit_item = gtk.MenuItem("Quit")
        self.quit_item.connect("activate", self.menu_action_quit)
        self.quit_item.show()
        self.menu.append(self.quit_item)
        
    def menu_action_push(self, widget):
        self.action_push()    
    def menu_action_pull(self, widget):
        self.action_pull()  
    def menu_action_open_preferences(self, widget):
        self.action_open_preferences()
    def menu_action_open_about(self, widget):
        self.action_open_about()    
    def menu_action_quit(self, widget):
        self.action_quit()  
        sys.exit(0)
        
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def action_push(self):
        pass
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def action_pull(self):
        pass
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def action_open_preferences(self):
        pass
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def action_open_about(self):
        pass
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def action_quit(self):
        pass
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def ready(self):
        pass


    def main(self):
        self.ready()
        gtk.main()
            
    @dbus.service.method('net.mms_projects.copyit.DesktopIntegration')
    def hello(self):
        self.HelloSignal('Hello')
        self.ind.set_status(appindicator.STATUS_ATTENTION)
        return "Hello,World!"
    
    @dbus.service.method('net.mms_projects.copyit.DesktopIntegration', in_signature='ss')
    def setup(self, icon, attention_icon):
        self.ind = appindicator.Indicator("new-gmail-indicator",
                                           icon,
                                           appindicator.CATEGORY_APPLICATION_STATUS)
        self.ind.set_status(appindicator.STATUS_ACTIVE)
        self.ind.set_attention_icon(attention_icon)

        self.menu_setup()
        self.ind.set_menu(self.menu)

if __name__ == "__main__":
    DBusGMainLoop(set_as_default=True)
    indicator = DesktopIntegration()
    indicator.main()
