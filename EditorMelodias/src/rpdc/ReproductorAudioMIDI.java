package rpdc;
import javax.sound.midi.*;
import entidades.Figura;
import entidades.NotaMusical;

public class ReproductorAudioMIDI {
    private static final int VELOCIDAD = 100; // Volumen
    private static final int CANAL_PIANO = 0; // Canal MIDI para piano

    // Mapeo de notas a valores MIDI (DO=60, RE=62, etc.)
    private static final int[] NOTAS_MIDI = {60, 62, 64, 65, 67, 69, 71};

    // Reproduce una nota musical
    public static void reproducirNota(NotaMusical notaMusical) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel piano = synth.getChannels()[CANAL_PIANO];

            int notaMidi = NOTAS_MIDI[notaMusical.getNota().ordinal()] + (notaMusical.getOctava() - 4) * 12;
            int duracion = obtenerDuracion(notaMusical.getFigura());

            piano.noteOn(notaMidi, VELOCIDAD);
            Thread.sleep(duracion);
            piano.noteOff(notaMidi);

            synth.close();
        } catch (Exception e) {
            System.err.println("Error al reproducir nota: " + e.getMessage());
        }
    }

    private static int obtenerDuracion(Figura figura) {
        switch (figura) {
            case REDONDA: return 1600; // 1.6 seg
            case BLANCA: return 800;   // 0.8 seg
            case NEGRA: return 400;    // 0.4 seg
            case CORCHEA: return 200;  // 0.2 seg
            default: return 400;
        }
    }
}