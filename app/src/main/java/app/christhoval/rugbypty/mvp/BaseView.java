package app.christhoval.rugbypty.mvp;

/**
 * Interfaz de comportamiento general de vistas
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
