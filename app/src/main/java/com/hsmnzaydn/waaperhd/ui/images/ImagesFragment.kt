package com.hsmnzaydn.waaperhd.ui.images

import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.basefy.base_mvp.BaseFragment
import com.basefy.core_utility.onInitGrid
import com.basefy.core_utility.pagenation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.hsmnzaydn.waaperhd.databinding.FragmentImagesBinding
import com.hsmnzaydn.waaperhd.image.domain.entities.Image
import com.hsmnzaydn.waaperhd.ui.adapters.ImagesAdapter
import com.hsmnzaydn.waaperhd.ui.controller
import com.hsmnzaydn.waaperhd.ui.image_detail.ImageDetailFragment
import javax.inject.Inject


class ImagesFragment : BaseFragment<FragmentImagesBinding>(), ImagesContract.View {
    @Inject
    lateinit var presenter: ImagesContract.Presenter<ImagesContract.View>


    private lateinit var imageAdapter: ImagesAdapter
    private lateinit var mInterstitialAd:InterstitialAd

    override fun loadDataToList(response: List<Image>?) {
        imageAdapter.items = response!!

        binding!!.fragmentImageContentLoadingProgressbar.run {
            visibility = View.GONE
        }

        imageAdapter.updateList(binding!!.fragmentImagesRecylerview)

        imageAdapter.onItemClick { it, position, layoutId ->
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }

            it.id?.let { it1 ->
                ImageDetailFragment.getImageDetailInstance(
                    it1,
                    imageAdapter.items[position].imagePath!!
                )
            }?.let { it2 ->
                controller.navigate(
                    it2
                )
            }
        }

    }

    override fun showBottomLoadin() {
        binding!!.fragmentImageContentLoadingProgressbar.run {
            visibility = View.VISIBLE
        }
    }

    override fun initUI() {
        binding = FragmentImagesBinding.inflate(layoutInflater)
        presenter.onAttach(this)

        initImageAdapter()

        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd.adUnitId = "ca-app-pub-7491116475843767/2697949096"
        mInterstitialAd.loadAd(AdRequest.Builder().build())


    }

    private fun initImageAdapter() {
        imageAdapter = ImagesAdapter(activity!!)

        binding!!.fragmentImagesRecylerview.setItemViewCacheSize(20)
        binding!!.fragmentImagesRecylerview.setHasFixedSize(true)
        binding!!.fragmentImagesRecylerview.isDrawingCacheEnabled = true
        binding!!.fragmentImagesRecylerview.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

        imageAdapter.onInitGrid(
            binding!!.fragmentImagesRecylerview,
            column = 3
        )

        imageAdapter.reciveBottom {
            presenter.getImages()
        }


        presenter.getImages()
    }

    override fun againOpened() {
    }


}
