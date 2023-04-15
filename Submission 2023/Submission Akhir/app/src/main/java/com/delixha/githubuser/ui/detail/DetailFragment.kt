package com.delixha.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.delixha.githubuser.R
import com.delixha.githubuser.data.local.entity.UserEntity
import com.delixha.githubuser.data.remote.response.DetailUserResponse
import com.delixha.githubuser.databinding.FragmentDetailBinding
import com.delixha.githubuser.utils.FavoriteViewModelFactory
import com.delixha.githubuser.utils.SectionsPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by activityViewModels<DetailViewModel> {
        FavoriteViewModelFactory.getInstance(requireActivity())
    }
    private var username: String = ""
    private var isFavorite: Boolean = false
    private var userData: UserEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = DetailFragmentArgs.fromBundle(arguments as Bundle).username
        binding.tvUsername.text = username
        detailViewModel.getDetailUser(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.detailUser.observe(viewLifecycleOwner) { detailUser ->
            setUserDetailData(detailUser)
        }

        detailViewModel.isFavoriteUser(username).observe(viewLifecycleOwner) { user ->
            if (user != null) {
                setFavorite(true)
            } else {
                setFavorite(false)
            }
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        detailViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.ivBack.setOnClickListener(this)
        binding.ivFavorite.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                binding.ivBack.findNavController().popBackStack()
            }
            R.id.iv_favorite -> {
                if (isFavorite) {
                    detailViewModel.delete(userData!!)
                    Toast.makeText(
                        requireContext(),
                        "Berhasil menghapus user favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    setFavorite(false)
                } else {
                    detailViewModel.insert(userData!!)
                    Toast.makeText(
                        requireContext(),
                        "Berhasil menambahkan user favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    setFavorite(true)
                }
            }
            R.id.iv_share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "[ GitHubUser Share ]\n\nSee @${username} on GitHub"
                    )
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserDetailData(user: DetailUserResponse) {
        Glide.with(binding.root)
            .load("https://github.githubassets.com/images/modules/site/home/globe.jpg")
            .placeholder(R.drawable.ic_loading)
            .into(binding.background)
        Glide.with(binding.root)
            .load(user.avatarUrl)
            .placeholder(R.drawable.ic_loading)
            .into(binding.avatarUser)
        if (user.name != null) {
            binding.tvName.text = user.name
        } else {
            binding.tvName.text = user.login
        }
        binding.tvName.visibility = View.VISIBLE
        if (user.bio != null) {
            binding.tvBio.text = user.bio
            binding.tvBio.visibility = View.VISIBLE
        } else {
            binding.tvBio.text = "-"
            binding.tvBio.visibility = View.VISIBLE
        }
        if (user.company != null) {
            binding.tvCompany.visibility = View.VISIBLE
            binding.tvCompanyUser.text = user.company
            binding.tvCompanyUser.visibility = View.VISIBLE
        } else {
            binding.tvCompany.visibility = View.VISIBLE
            binding.tvCompanyUser.text = "-"
            binding.tvCompanyUser.visibility = View.VISIBLE
        }
        if (user.location != null) {
            binding.tvLocation.visibility = View.VISIBLE
            binding.tvLocationUser.text = user.location
            binding.tvLocationUser.visibility = View.VISIBLE
        } else {
            binding.tvLocation.visibility = View.VISIBLE
            binding.tvLocationUser.text = "-"
            binding.tvLocationUser.visibility = View.VISIBLE
        }
        binding.tvRepository.visibility = View.VISIBLE
        binding.tvRepositoryUser.text = user.publicRepos.toString()
        binding.tvRepositoryUser.visibility = View.VISIBLE
        binding.tvFollowers.visibility = View.VISIBLE
        binding.tvFollowersUser.text = user.followers.toString()
        binding.tvFollowersUser.visibility = View.VISIBLE
        binding.tvFollowing.visibility = View.VISIBLE
        binding.tvFollowingUser.text = user.following.toString()
        binding.tvFollowingUser.visibility = View.VISIBLE

        userData = UserEntity(
            user.login.toString(),
            user.avatarUrl.toString(),
        )
    }

    private fun setFavorite(state: Boolean) {
        val ivFavorite = binding.ivFavorite
        if (state) {
            isFavorite = true
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite))
        } else {
            isFavorite = false
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite_border))
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}