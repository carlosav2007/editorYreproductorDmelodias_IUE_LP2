package entidades;

import rpdc.ListaLigada;

public class Melodia {
    private String nombre;
    private ListaLigada<NotaMusical> notas;

    public Melodia(String nombre) {
        this.nombre = nombre;
        this.notas = new ListaLigada<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ListaLigada<NotaMusical> getNotas() {
        return notas;
    }

    public void agregarNota(NotaMusical nota) {
        notas.agregar(nota);
    }

    public void eliminarNota(int indice) {
        notas.eliminar(indice);
    }

    public NotaMusical obtenerNota(int indice) {
        return notas.obtener(indice);
    }

    public void modificarNota(int indice, NotaMusical nuevaNota) {
        notas.modificar(indice, nuevaNota);
    }
}