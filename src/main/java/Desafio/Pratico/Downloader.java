package Desafio.Pratico;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Downloader {
    private List<String> url = new ArrayList<>();

    public void baixaMusica(String url, String pathToDownload) {
        YoutubeDownloader downloader = new YoutubeDownloader();

        try {
            // ID do vídeo do YouTube
            RequestVideoInfo request = new RequestVideoInfo(url);
            Response<VideoInfo> response = downloader.getVideoInfo(request);
            VideoInfo video = response.data();
            // ID do vídeo no YouTube (exemplo)
            String nomeVideo = video.details().title();
            // Obtém informações do vídeo
            AudioFormat audioFormat = video.bestAudioFormat();

            // Seleciona o melhor formato de áudio disponível

            File outputDir = new File(pathToDownload); // Diretório de saída
            RequestVideoFileDownload requestDownload = new RequestVideoFileDownload(audioFormat)
                    .saveTo(outputDir)
                    .renameTo(nomeVideo+".mp3")
                    .overwriteIfExists(true);

            Response<File> responseDownload = downloader.downloadVideoFile(requestDownload);
            File audioFile = responseDownload.data();
            System.out.println("Download finalizado!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String buscaNomeMusica(String url) {
        YoutubeDownloader downloader = new YoutubeDownloader();
            // ID do vídeo do YouTube
            RequestVideoInfo request = new RequestVideoInfo(url);
            Response<VideoInfo> response = downloader.getVideoInfo(request);
            VideoInfo video = response.data();
            // ID do vídeo no YouTube (exemplo)

            //Obtem o nome da música
            return video.details().title();
    }

    public void adicionaMusica(String musica) {
        try{
            int indice = this.url.indexOf(musica);
            if(indice >= 0) {
                throw new InvalidParameterException("Música já adicionada.");
            } else {
                this.url.add(musica);
            }
        } catch (InvalidParameterException e) {
            System.err.println(e.getMessage());
        }

    }

    public List<String> getUrl() {
        return this.url;
    }
}
