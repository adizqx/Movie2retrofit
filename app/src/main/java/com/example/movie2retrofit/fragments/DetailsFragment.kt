package com.example.movie2retrofit.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.bumptech.glide.Glide
import com.example.movie2retrofit.adapter.castAdapter
import com.example.movie2retrofit.api.ApiInterface
import com.example.movie2retrofit.castModel.castModel
import com.example.movie2retrofit.databinding.FragmentDetailsBinding
import com.example.movie2retrofit.modelDetail.movieDetails
import com.example.movie2retrofit.trailerModel.trailerModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    private val castAdapter by lazy { castAdapter(requireContext()) }
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentDetailsBinding.inflate(inflater, container, false)


        val args = this.arguments
        val id = args?.getInt("key")
        Log.d("testing", "id $id")

        val apiInterface = id?.let {
            ApiInterface.create().getMoviesDetails(it, "67ff61f491eeb461f0f90226a984be92")
        }
        apiInterface?.enqueue(object : Callback<movieDetails> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<movieDetails>, response: Response<movieDetails>) {
                binding.tvTitle.text = response.body()?.title
                binding.tvConent.text = response.body()?.overview
                binding.tvGenre.text = response.body()?.genres?.get(0)?.name
                binding.tvPg.text = response.body()?.genres?.get(0)?.id.toString() + "+"
                Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/w500" + response.body()?.backdrop_path)
                    .into(binding.poster)
                binding.ratingBar.rating = response.body()?.vote_average?.toFloat()!!
                binding.tvReviews.text = response.body()?.vote_count.toString() + " reviews"
            }

            override fun onFailure(call: Call<movieDetails>, t: Throwable) {
                Log.d("test", "onFailure:${t.message}")
            }

        })
        binding.backButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        val apiInterfaceCast = id?.let {
            ApiInterface.create().getCast(it, "67ff61f491eeb461f0f90226a984be92")
        }
        apiInterfaceCast?.enqueue(object : Callback<castModel> {
            override fun onResponse(call: Call<castModel>, response: Response<castModel>) {
                binding.recyclerViewCast.adapter = castAdapter
                binding.recyclerViewCast.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                response.body()?.cast?.let { castAdapter.setCastList(it) }
            }

            override fun onFailure(call: Call<castModel>, t: Throwable) {
                Log.d("test", "onFailure:${t.message}")
            }

        })


        val apiInterfaceTrailer = id?.let {
            ApiInterface.create().getTrailer(it, "67ff61f491eeb461f0f90226a984be92")
        }
        apiInterfaceTrailer?.enqueue(object : Callback<trailerModel> {
            override fun onResponse(call: Call<trailerModel>, response: Response<trailerModel>) {

                val videoUrl = "https://www.youtube.com/watch?v=" + response.body()?.results?.get(0)?.key

                initPlayer(videoUrl)
            }

            override fun onFailure(call: Call<trailerModel>, t: Throwable) {
                Log.d("test", "onFailure:${t.message}")
            }

        })
        return binding.root
    }

    fun initPlayer(videoUrl: String) {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.exoplayer.player = player


        object : YouTubeExtractor(requireContext()) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles != null) {
                    val itag = 137
                    val audioTag = 140
                    val videoUrl = ytFiles[itag].url
                    val audioUrl = ytFiles[audioTag].url

                    val audioSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(audioUrl))
                    val videoSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(videoUrl))

                    player!!.setMediaSource(
                        MergingMediaSource(
                            true,
                            videoSource,
                            audioSource
                        ), true
                    )
                    player!!.prepare()
                    player!!.playWhenReady = playWhenReady
                    player!!.seekTo(currentWindow, playbackPosition)
                }
            }

        }.extract(videoUrl, false, true)
    }

//    override fun onStart() {
//        super.onStart()
//        if(Util.SDK_INT >= 24)
//            initPlayer("")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if(Util.SDK_INT < 24 || player == null )
//        {
//            initPlayer()
//        }
//    }

    override fun onPause() {
        super.onPause()
        if(Util.SDK_INT < 24) releasePlayer()
    }

    override fun onStop() {
        if(Util.SDK_INT < 24) releasePlayer()
        super.onStop()
    }

    private fun releasePlayer() {
        if(player != null)
        {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }


}