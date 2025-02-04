document.addEventListener("DOMContentLoaded", function () {
    // Ensure these elements exist before adding event listeners
    const enrollmentFile = document.getElementById("enrollmentFile");
    const insuranceFile = document.getElementById("insuranceFile");
    const newInsuranceFile = document.getElementById("newInsuranceFile");

    if (enrollmentFile) enrollmentFile.addEventListener("change", validateImages);
    if (insuranceFile) insuranceFile.addEventListener("change", validateImages);
    if (newInsuranceFile) newInsuranceFile.addEventListener("change", validateImages);
});

function navigateTo(section) {
    const newPatientSection = document.getElementById("newPatientSection");
    const existingPatientSection = document.getElementById("existingPatientSection");

    if (!newPatientSection || !existingPatientSection) return;

    // Hide both sections first
    newPatientSection.classList.add("d-none");
    existingPatientSection.classList.add("d-none");

    // Show the correct section
    if (section === "new") {
        newPatientSection.classList.remove("d-none");
    } else if (section === "existing") {
        existingPatientSection.classList.remove("d-none");
    }
}

function validateImages(input) {
    if (!input || !input.files) {
        console.error("No file input detected.");
        return;
    }

    const files = input.files;
    if (files.length === 0) {
        console.error("No files selected.");
        return;
    }

    console.log("Validating", files.length, "files.");
    for (let file of files) {
        const img = new Image();
        img.src = URL.createObjectURL(file);
        img.onload = function () {
            if (this.width < 500 || this.height < 500) {
                alert("Image resolution too low. Please upload a clearer image.");
                input.target.value = ""; // Clear invalid file
            }
        };
    }
}


function uploadEnrollment() {
    let fileInput = document.getElementById("enrollmentFile");
    if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
        alert("Please upload at least one enrollment form.");
        return;
    }

    let formData = new FormData();
    for (let file of fileInput.files) {
        formData.append("files", file);
    }

    fetch("/patient/enrollment/upload", { // Ensure correct backend URL
        method: "POST",
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server Error: ${response.status} ${response.statusText}`);
            }
            return response.text(); // First, read response as text
        })
        .then(text => {
            try {
                return JSON.parse(text); // Try parsing JSON
            } catch (error) {
                throw new Error("Invalid JSON response from server: " + text);
            }
        })
        .then(data => {
            let parsedDataDiv = document.getElementById("parsedData");
            let insuranceSection = document.getElementById("insuranceSection");

            if (!data || !data.fullName) {
                parsedDataDiv.innerHTML = "<p class='text-danger'>No valid details extracted. Please upload a clearer form.</p>";
            } else {
                parsedDataDiv.innerHTML = generatePatientForm(data);
                if (insuranceSection) {
                    insuranceSection.classList.remove("d-none");
                }
            }
        })
        .catch(error => {
            console.error("Upload failed:", error);
            alert("Upload failed: " + error.message);
        });
}


function generatePatientForm(patient) {
    return `
        <h4>Review & Edit Patient Details</h4>
        <form id="patientDetailsForm">
            <label class="form-label">Full Name:</label>
            <input type="text" class="form-control" value="${patient.fullName}" disabled>
            
            <label class="form-label">Date of Birth:</label>
            <input type="text" class="form-control" value="${patient.dateOfBirth}" disabled>
            
            <label class="form-label">SSN:</label>
            <input type="text" class="form-control" value="${patient.ssn || ""}" disabled>
            
            <label class="form-label">Patient ID:</label>
            <input type="text" class="form-control" value="${patient.pid}" disabled>
            
            <label class="form-label">Address:</label>
            <input type="text" class="form-control" value="${patient.address}">
            
            <label class="form-label">Contact Number:</label>
            <input type="text" class="form-control" value="${patient.contactNumber}">
            
            <button type="button" class="btn btn-primary mt-3" onclick="submitEnrollment('${patient.pid}')">Submit</button>
        </form>
    `;
}

function submitEnrollment(pid) {
    let form = document.getElementById("patientDetailsForm");
    let patientData = {
        pid: pid,
        address: form.elements[4].value,
        contactNumber: form.elements[5].value
    };

    fetch("/patient/enrollment/submit", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(patientData)
    })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            document.getElementById("insuranceSection").classList.remove("d-none");
        })
        .catch(error => console.error("Error:", error));
}

function uploadInsurance() {
    let files = document.getElementById("insuranceFile").files;
    if (files.length === 0) {
        alert("Please upload at least one insurance card.");
        return;
    }

    let formData = new FormData();
    for (let file of files) {
        formData.append("files", file);
    }

    fetch("/patient/insurance/upload?pid=" + document.getElementById("patientDetailsForm").elements[3].value, {
        method: "POST",
        body: formData
    })
        .then(response => JSON.parse(response.text()))
        .then(data => {
            if (!data || !data.policyNumber) {
                document.getElementById("insuranceData").innerHTML = "<p class='text-danger'>No valid insurance details extracted.</p>";
            } else {
                document.getElementById("insuranceData").innerHTML = `<p><strong>Provider:</strong> ${data.insuranceProvider}</p><p><strong>Policy Number:</strong> ${data.policyNumber}</p>`;
            }
        })
        .catch(error => console.error("Error:", error));
}

function fetchPatientDetails() {
    let patientId = document.getElementById("patientId").value.trim();
    if (!patientId) {
        alert("Please enter SSN or Patient ID.");
        return;
    }

    fetch("/patient/enrollment/details?patientId=" + patientId)
        .then(response => response.json())
        .then(data => {
            if (!data || !data.fullName) {
                document.getElementById("patientDetails").innerHTML = "<p class='text-danger'>No records found.</p>";
            } else {
                document.getElementById("patientDetails").innerHTML = generatePatientForm(data);
                document.getElementById("existingInsuranceSection").classList.remove("d-none");
                fetchInsuranceDetails(data.pid);
            }
        })
        .catch(error => console.error("Error:", error));
}

function fetchInsuranceDetails(pid) {
    fetch("/patient/insurance/details?patientId=" + pid)
        .then(response => response.json())
        .then(data => {
            if (!data || !data.policyNumber) {
                document.getElementById("existingInsuranceData").innerHTML = "<p class='text-warning'>No active insurance found.</p>";
            } else {
                document.getElementById("existingInsuranceData").innerHTML = `<p><strong>Provider:</strong> ${data.insuranceProvider}</p><p><strong>Policy Number:</strong> ${data.policyNumber}</p>`;
            }
        })
        .catch(error => console.error("Error:", error));
}

function uploadNewInsurance() {
    let files = document.getElementById("newInsuranceFile").files;
    if (files.length === 0) {
        alert("Please upload at least one insurance card.");
        return;
    }

    let pid = document.getElementById("patientDetailsForm").elements[3].value;
    let formData = new FormData();
    for (let file of files) {
        formData.append("files", file);
    }

    fetch("/patient/insurance/upload?pid=" + pid, {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (!data || !data.policyNumber) {
                document.getElementById("existingInsuranceData").innerHTML += "<p class='text-danger'>No valid insurance details extracted.</p>";
            } else {
                document.getElementById("existingInsuranceData").innerHTML += `<p><strong>Provider:</strong> ${data.insuranceProvider}</p><p><strong>Policy Number:</strong> ${data.policyNumber}</p>`;
            }
        })
        .catch(error => console.error("Error:", error));
}
