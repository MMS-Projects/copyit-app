#!/usr/bin/env python
try:
    from gi.repository import (
        Dbusmenu,
        SyncMenu,
        GObject
    )
    use_syncmenu = True
except:
    use_syncmenu = False
    
from dbus.mainloop.glib import DBusGMainLoop
import appindicator
import dbus
import dbus.service
import gtk
import sys
import time

class DesktopIntegrationSyncMenu:
    def __init__(self, main):
        self.main = main
        self.main_loop = GObject.MainLoop()
    def set_enabled(self, state):
        print "Syncing has been " + ("enabled" if state else "disabled")
        self._ignore_status_event = True
        self.app.set_paused(not state)
    def set_state(self, state):
        print "Sync state has been set to " + state
        self._ignore_status_event = True
        if state == "idle":
            self.app.set_state(SyncMenu.State.IDLE)
        elif state == "syncing":
            self.app.set_state(SyncMenu.State.SYNCING)
        elif state == "error":
            self.app.set_state(SyncMenu.State.ERROR)            
    def loop(self):
        self.main_loop.run()
    def setup(self, icon, attention_icon):
        self.app = SyncMenu.App.new("copyit.desktop")
        self.app.connect("notify::paused", self.sync_stage_change)
        self.setup_menu()
    def setup_menu(self):
        self.menu_item_push = Dbusmenu.Menuitem()
        self.menu_item_push.property_set(Dbusmenu.MENUITEM_PROP_LABEL, "Copy It")
        self.menu_item_pull = Dbusmenu.Menuitem()
        self.menu_item_pull.property_set(Dbusmenu.MENUITEM_PROP_LABEL, "Paste It")
        self.menu_item_open_preferences = Dbusmenu.Menuitem()
        self.menu_item_open_preferences.property_set(Dbusmenu.MENUITEM_PROP_LABEL, "Preferences")
        self.menu_item_open_about = Dbusmenu.Menuitem()
        self.menu_item_open_about.property_set(Dbusmenu.MENUITEM_PROP_LABEL, "About")
        self.menu_item_quit = Dbusmenu.Menuitem()
        self.menu_item_quit.property_set(Dbusmenu.MENUITEM_PROP_LABEL, "Quit")
        
        self.menu_item_push.connect(Dbusmenu.MENUITEM_SIGNAL_ITEM_ACTIVATED, self.menu_action_push)
        self.menu_item_pull.connect(Dbusmenu.MENUITEM_SIGNAL_ITEM_ACTIVATED, self.menu_action_pull)
        self.menu_item_open_preferences.connect(Dbusmenu.MENUITEM_SIGNAL_ITEM_ACTIVATED, self.menu_action_open_preferences)
        self.menu_item_open_about.connect(Dbusmenu.MENUITEM_SIGNAL_ITEM_ACTIVATED, self.menu_action_open_about)
        self.menu_item_quit.connect(Dbusmenu.MENUITEM_SIGNAL_ITEM_ACTIVATED, self.menu_action_quit)
        
        self.root_menu = Dbusmenu.Menuitem()
        self.root_menu.child_append(self.menu_item_push)
        self.root_menu.child_append(self.menu_item_pull)
        self.root_menu.child_append(self.menu_item_open_preferences)
        self.root_menu.child_append(self.menu_item_open_about)
        self.root_menu.child_append(self.menu_item_quit)
        
        self.server = Dbusmenu.Server()
        self.server.set_root(self.root_menu)
        self.app.set_menu(self.server)
    def menu_action_push(self, *args):
        self.main.action_push()    
    def menu_action_pull(self, *args):
        self.main.action_pull()  
    def menu_action_open_preferences(self, *args):
        self.main.action_open_preferences()
    def menu_action_open_about(self, *args):
        self.main.action_open_about()    
    def menu_action_quit(self, *args):
        self.main.action_quit()  
        sys.exit(0)
    def sync_stage_change(self, app, paused):
        if self._ignore_status_event:
            self._ignore_status_event = False
            return
        self.main.on_enable_change(not self.main.get_enabled())
        
class DesktopIntegrationAppIndicator:
    def __init__(self, main):
        self.main = main
    def set_enabled(self, state):
        pass
    def set_state(self, state):
        if state == "idle":
            self.ind.set_status(appindicator.STATUS_ACTIVE)
        elif state == "syncing":
            pass
        elif state == "error":
            self.ind.set_status(appindicator.STATUS_ATTENTION)
    def loop(self):
        gtk.main()
    def setup(self, icon, attention_icon):
        self.ind = appindicator.Indicator("copyit",
                                           icon,
                                           appindicator.CATEGORY_APPLICATION_STATUS)
        self.ind.set_status(appindicator.STATUS_ACTIVE)
        self.ind.set_attention_icon(attention_icon)

        self.setup_menu()
    def setup_menu(self):
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
        self.ind.set_menu(self.menu)
    def menu_action_push(self, widget):
        self.main.action_push()    
    def menu_action_pull(self, widget):
        self.main.action_pull()  
    def menu_action_open_preferences(self, widget):
        self.main.action_open_preferences()
    def menu_action_open_about(self, widget):
        self.main.action_open_about()    
    def menu_action_quit(self, widget):
        self.main.action_quit()  
        sys.exit(0)
             
class DesktopIntegrationDummy:
    def __init__(self, main):
        self.main = main
        self.main_loop = GObject.MainLoop()
    def set_enabled(self, state):
        print "Syncing has been " + ("enabled" if state else "disabled")
    def set_state(self, state):
        print "Sync state has been set to " + state
    def setup(self, icon, attention_icon):
        pass
    def loop(self):
        self.main_loop.run()

class DesktopIntegration(dbus.service.Object):
    enabled = None
    state = None
    def __init__(self):
        self.bus_name = dbus.service.BusName('net.mms_projects.copyit.DesktopIntegration', bus=dbus.SessionBus())
        dbus.service.Object.__init__(self, self.bus_name, '/')
        
        if use_syncmenu:
            self.integration = DesktopIntegrationSyncMenu(self)
        else:
            self.integration = DesktopIntegrationAppIndicator(self)
        # self.integration = DesktopIntegrationDummy(self)
    @dbus.service.method('net.mms_projects.copyit.DesktopIntegration', in_signature='b')
    def set_enabled(self, state):
        if state == self.enabled:
            print "Sync already set to " + ("enabled" if state else "disabled") + ". Not changing it"
            return
        self.enabled = state
        self.integration.set_enabled(state)
    def get_enabled(self):
        return self.enabled    
    @dbus.service.method('net.mms_projects.copyit.DesktopIntegration', in_signature='s')
    def set_state(self, state):
        if state == self.state:
            print "Sync state already set to " + state + ". Not changing it"
            return
        self.state = state
        self.integration.set_state(state)
    def get_state(self):
        return self.state    
    def on_enable_change(self, state):
        self.enabled = state
        if self.enabled:
            self.action_enable_sync()
        else:
            self.action_disable_sync()
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
    def action_enable_sync(self):
        print "Enabled sync"
        pass
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def action_disable_sync(self):
        print "Disabled sync"
        pass
    @dbus.service.signal('net.mms_projects.copyit.DesktopIntegration')
    def ready(self):
        pass
    def main(self):
        self.ready()
        self.integration.loop()
    @dbus.service.method('net.mms_projects.copyit.DesktopIntegration')
    def test(self):
        delay = 1
        actual_enabled = self.enabled;
        actual_state = self.state
        
        self.set_enabled(True)
        time.sleep(delay)
        self.set_enabled(False)
        time.sleep(delay)
        self.set_enabled(actual_enabled)
        time.sleep(delay)
        
        self.set_state("idle")
        time.sleep(delay)
        self.set_state("syncing")
        time.sleep(delay)
        self.set_state("error")
        time.sleep(delay)
        self.set_state(actual_state)
        time.sleep(delay)
    @dbus.service.method('net.mms_projects.copyit.DesktopIntegration', in_signature='ss')
    def setup(self, icon, attention_icon):
        self.integration.setup(icon, attention_icon)
        self.set_state("idle")

if __name__ == "__main__":
    DBusGMainLoop(set_as_default=True)
    indicator = DesktopIntegration()
    indicator.main()
