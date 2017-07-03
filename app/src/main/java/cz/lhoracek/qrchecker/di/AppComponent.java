package cz.lhoracek.qrchecker.di;


import javax.inject.Singleton;

import cz.lhoracek.qrchecker.screens.home.MainActivity;
import cz.lhoracek.qrchecker.screens.settings.SettingActivity;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(SettingActivity activity);
}