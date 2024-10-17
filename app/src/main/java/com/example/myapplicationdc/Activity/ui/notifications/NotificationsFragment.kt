package com.example.myapplicationdc.Activity.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationdc.Domain.Appointment
import com.example.myapplicationdc.databinding.FragmentNotificationsBinding
import com.google.firebase.database.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("appointment")
        fetchAppointments(0.toString()) // Fetch appointments for doctorId = 0
    }

    // Function to fetch appointments where doctorId = 0 from Firebase
    private fun fetchAppointments(doctorId: String) {
        // Query to find appointments for the specified doctorId
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d("NotificationsFragment", "No appointments found for doctorId: $doctorId")
                    return
                }

                // Clear table rows before updating with new data
                binding.tableLayout.removeAllViews()

                // Iterate through the appointments
                for (appointmentSnapshot in dataSnapshot.children) {
                    // Each appointmentSnapshot represents an appointment node
                    val appointmentData = appointmentSnapshot.value as? Map<*, *>
                    val appointment = appointmentData?.let {
                        Appointment(
                            appointmentId = it["appointmentId"] as? Int ?: 0,
                            appointmentDate = it["appointmentDate"] as? String ?: "Unknown",
                            doctorId = it["doctorId"] as? Int ?: 0,
                            patientId = it["patientId"] as? Int ?: 0,
                            doctorName = it["doctorName"] as? String ?: "Unknown",
                            doctorImage = it["doctorImage"] as? String ?: "Unknown",
                            location = it["location"] as? String ?: "Unknown"
                        )
                    } ?: continue

                    // Log the fetched appointment
                    Log.d("NotificationsFragment", "Fetched appointment: ${appointment.appointmentDate}, ID: ${appointment.appointmentId}")

                    // Check if the doctorId matches
                    if (appointment.doctorId.toString() == doctorId) {
                        // Fetch patient's name and medical history using the patientId from the appointment
                        fetchPatientDetails(appointment.patientId!!) { patientName, medicalHistory ->
                            addRowToTable(patientName, appointment.appointmentDate!!, medicalHistory)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("NotificationsFragment", "Error fetching appointments: ${databaseError.message}")
            }
        })
    }

    // Function to fetch patient details based on patientId
    private fun fetchPatientDetails(patientId: Int, callback: (String, String) -> Unit) {
        val patientReference = FirebaseDatabase.getInstance().getReference("Patients")

        patientReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var found = false
                for (patientSnapshot in dataSnapshot.children) {
                    val id = patientSnapshot.child("id").getValue(Int::class.java) ?: 0

                    if (id == patientId) {
                        found = true
                        // Retrieve patient name and medical history
                        val patientName = patientSnapshot.child("pname").getValue(String::class.java) ?: "Unknown"
                        val medicalHistory = patientSnapshot.child("medicalHistory").getValue(String::class.java) ?: "No history"

                        // Log the retrieved values for debugging
                        Log.d("NotificationsFragment", "Fetched patientName: $patientName, medicalHistory: $medicalHistory")

                        callback(patientName, medicalHistory)
                        return
                    }
                }

                if (!found) {
                    Log.d("NotificationsFragment", "Patient ID $patientId not found")
                    callback("Unknown", "No history")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("NotificationsFragment", "Error fetching patient details: ${databaseError.message}")
            }
        })
    }

    // Function to add rows dynamically to the table layout
    private fun addRowToTable(patientName: String, appointmentDay: String, medicalHistory: String) {
        val tableLayout: TableLayout = binding.tableLayout
        val row = TableRow(context)

        val nameTextView = TextView(context).apply {
            text = patientName
            setPadding(8, 8, 8, 8)
        }
        row.addView(nameTextView)

        val dayTextView = TextView(context).apply {
            text = appointmentDay
            setPadding(8, 8, 8, 8)
        }
        row.addView(dayTextView)

        val historyTextView = TextView(context).apply {
            text = medicalHistory
            setPadding(8, 8, 8, 8)
        }
        row.addView(historyTextView)

        // Add the row to the TableLayout
        tableLayout.addView(row)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
