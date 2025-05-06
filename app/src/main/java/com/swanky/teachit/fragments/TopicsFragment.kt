package com.swanky.teachit.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.swanky.teachit.R
import com.swanky.teachit.adapters.TopicsAdapter
import com.swanky.teachit.databinding.BottomSheetAddTopicBinding
import com.swanky.teachit.databinding.FragmentTopicsBinding
import com.swanky.teachit.helpers.RecyclerItemAnimation
import com.swanky.teachit.models.Topic
import com.swanky.teachit.utils.gone
import com.swanky.teachit.utils.show
import com.swanky.teachit.viewmodels.TopicsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicsFragment : Fragment() {

    private var _dataBinding: FragmentTopicsBinding? = null
    private val dataBinding get() = _dataBinding!!
    private val viewModel: TopicsViewModel by viewModels()
    private var topicsAdapter: TopicsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_topics, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.master = this
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collapseFabButton()
        setupSearchView()
        observeLiveData()
        viewModel.getTopics()
    }


    fun previousPage() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    fun addTopic() {
        showAddBottomSheetDialog()
    }

    private fun showAddBottomSheetDialog() {
        val bottomDialogBinding =
            BottomSheetAddTopicBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.apply {
            setContentView(bottomDialogBinding.root)
            this.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            setCancelable(true)
        }

        // Define input fields ->
        val addTopicTitleTxt = bottomDialogBinding.addTopicTitleTxt
        val addTopicDescriptionTxt = bottomDialogBinding.addTopicDescTxt

        bottomDialogBinding.addTopicBotton.setOnClickListener {
            if (addTopicTitleTxt.text.isNullOrEmpty()) {
                bottomDialogBinding.textInputLayoutAddTopicTitle.error = "Konu başlığı giriniz."
            } else {
                viewModel.addTopic(
                    topic = Topic(
                        title = addTopicTitleTxt.text.toString(),
                        description = addTopicDescriptionTxt.text.toString()
                    )
                )
                // Close dialog
                bottomSheetDialog.dismiss()
            }
        }

        // Show dialog
        bottomSheetDialog.show()
    }

    private fun collapseFabButton() {
        val fabButton = dataBinding.addTopicButton
        Handler(Looper.getMainLooper()).postDelayed({
            fabButton.shrink()
        }, 1500)
    }

    private fun observeLiveData() {
        viewModel.topics.observe(viewLifecycleOwner) { topics ->
            if (topics.isNullOrEmpty()) {
                dataBinding.noResultTopicLayout.show()
                dataBinding.topicsRecyclerView.gone()
            } else {
                dataBinding.noResultTopicLayout.gone()
                dataBinding.topicsRecyclerView.show()
                setRecycler(topics)
            }

        }

        viewModel.errorGetLiveData.observe(viewLifecycleOwner) { error ->
            // Error handling
            if (error) {
                // Handle error
                println(error)
            }
        }

        viewModel.errorAddLiveData.observe(viewLifecycleOwner) { error ->
            // Error handling
            if (error) {
                // Handle error
                Toast.makeText(requireContext(), "Konu eklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
                println(error)
            } else {
                // Handle success
                Toast.makeText(requireContext(), "Konu eklendi", Toast.LENGTH_SHORT).show()
                // Refresh list
                viewModel.getTopics()
            }
        }
    }

    private fun setupSearchView() {
        dataBinding.topicsSearchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                topicsAdapter?.filter?.filter(newText)
                return false
            }
        })
    }

    private fun setRecycler(topicsList: List<Topic>) {
        val topicsRecycler = dataBinding.topicsRecyclerView
        topicsAdapter = TopicsAdapter(requireContext(), topicsList)

        topicsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = topicsAdapter
        }

        //Define animation for recycler
        val itemAnimation = RecyclerItemAnimation(topicsRecycler)
        itemAnimation.animateRecyclerAdd()
        itemAnimation.animateRecyclerRemove()
    }


    override fun onDestroy() {
        super.onDestroy()
        _dataBinding = null
    }

}