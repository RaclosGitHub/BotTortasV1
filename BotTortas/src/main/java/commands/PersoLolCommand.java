package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersoLolCommand implements Command {

    String commandName = "persolol";

    @Override
    public void execute(MessageReceivedEvent event) {
        ArrayList<String> nombresJugadores = new ArrayList<>();
        ArrayList<Integer> randomNumbers = new ArrayList<>();
        ArrayList<String> nombresJugadores2 = new ArrayList<>();
        Random random = new Random();
        List<Member> jugadoresCustom;
        VoiceChannel voiceChannel1 = event.getMember().getVoiceState().getChannel();
        jugadoresCustom = voiceChannel1.getMembers();

        for (Member jugador : jugadoresCustom) {
            nombresJugadores.add(jugador.getEffectiveName());
        }

        if (nombresJugadores.size() < 10) {
            nombresJugadores = fillWithRandoms(nombresJugadores);
        }

        for (int i = 0; i < 5; i++) {
            int number = random.nextInt(9 - 0 + 1) + 0;
            if (randomNumbers.contains(number)) {
                i--;
            } else {
                randomNumbers.add(number);
            }
        }
        for (Integer number : randomNumbers) {
            nombresJugadores2.add(nombresJugadores.get(number));
        }
        nombresJugadores.removeAll(nombresJugadores2);
        event.getChannel().sendMessage("Team Nattys: " + nombresJugadores.toString()).submit();
        event.getChannel().sendMessage("Team Chuzos: " + nombresJugadores2.toString()).submit();

        ArrayList<String> finalNombresJugadores = nombresJugadores;
        Thread thread = new Thread() {
            public void run() {
                event.getChannel().sendMessage("La partida empieza en: 9").submit();
                for (int i = 10; i > 0; i--) {
                    event.getChannel().editMessageById(event.getChannel().getLatestMessageId(), "La partida empieza en: " + (i - 1)).submit();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById("585590707826327578");
                for (String nombre : finalNombresJugadores) {
                    for (Member jugador : jugadoresCustom) {
                        if (jugador.getEffectiveName().equals(nombre)) {
                            voiceChannel.getGuild().moveVoiceMember(jugador, voiceChannel).submit();
                        }
                    }
                }

                VoiceChannel voiceChannel2 = event.getGuild().getVoiceChannelById("742834147772465162");
                for (String nombre : nombresJugadores2) {
                    for (Member jugador : jugadoresCustom) {
                        if (jugador.getEffectiveName().equals(nombre)) {
                            voiceChannel2.getGuild().moveVoiceMember(jugador, voiceChannel2).submit();
                        }
                    }
                }
                event.getChannel().sendMessage("Los equipos se han movido").submit();
            }
        };

        thread.start();
    }

    @Override
    public void info(MessageReceivedEvent event) {
        event.getChannel().sendMessage(":white_small_square: !" + commandName + ": Hace equipos random y los mueve a sus salas correspondientes").submit();
    }

    public ArrayList<String> fillWithRandoms(ArrayList<String> jugadoresCustom) {
        int i = 1;
        while (jugadoresCustom.size() < 10) {
            jugadoresCustom.add("Random" + i);
            i++;
        }
        return jugadoresCustom;
    }

    @Override
    public String getName() {
        return commandName;
    }
}
