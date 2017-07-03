package cz.lhoracek.qrchecker.di.activity;


import cz.lhoracek.qrchecker.di.PerActivity;
import cz.lhoracek.qrchecker.di.application.AppComponent;
import cz.lhoracek.qrchecker.screens.BaseActivity;
import cz.lhoracek.qrchecker.screens.home.MainActivity;
import cz.lhoracek.qrchecker.screens.settings.SettingsActivity;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(SettingsActivity activity);

}
