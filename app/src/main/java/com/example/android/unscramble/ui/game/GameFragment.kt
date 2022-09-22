package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragmen tempat game dimainkan, berisi logika game.
 */
class GameFragment : Fragment() {

    //objek dapat diakses dari layout game_fragment.xml
    private lateinit var binding: GameFragmentBinding

    // membuat view model pertamakali
    // Jika fragmen dibuat ulang, ia menerima dari class GameViewModel yang sama yang dibuat oleh
    // fragmen pertama.
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Mengembangkan file XML pada layout dan mengembalikan instance objek yang terikat
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // menyetel data view model untuk data binding(memungkinkan untuk layout bisa mengakses)
        // ke semua data di view model
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS
        // menentukan fragmen view sebagai lifecycle of binding
        // Ini digunakan agar pengikatan dapat mengamati pembaruan LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        // menyiapakan tombol peralihan untuk submit dan skip
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
    }

    /*
    * Memeriksa kata pengguna, dan memperbarui skor yang sesuai.
    * Menampilkan kata acak berikutnya.
    * Setelah kata terakhir, pengguna diperlihatkan Dialog dengan skor akhir.
    */
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    /*
     * Melompati kata saat ini tanpa mengubah skor.
     * Meningkatkan jumlah kata.
     * Setelah kata terakhir, pengguna diperlihatkan Dialog dengan skor akhir.
     */
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            showFinalScoreDialog()
        }
    }

    /*
     * membuat dan menampilakan alert dialok dan skor final
     */
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.congratulations))
                .setMessage(getString(R.string.you_scored, viewModel.score.value))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.exit)) { _, _ ->
                    exitGame()
                }
                .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                    restartGame()
                }
                .show()
    }

    /*
     * Inisialisasi ulang data di ViewModel dan perbarui tampilan dengan data baru, untuk
     * mulai ulang permainan.
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    /*
     * keluar dari game
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    *Menyetel dan menreset ulang status kesalahan bidang teks.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }
}
