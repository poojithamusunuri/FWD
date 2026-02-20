let records = [];
let undoStack = [];


function addRecord() {

    let facility = document.getElementById("facility").value;
    let rule = document.getElementById("rule").value;
    let status = document.getElementById("status").value;
    let date = document.getElementById("date").value;

    if (!facility || !rule || !status || !date) {
        alert("Please fill all fields");
        return;
    }

    let record = { facility, rule, status, date };

    records.push(record);
    undoStack.push(record);

    display(records);

    document.getElementById("rule").value = "";
    document.getElementById("date").value = "";
}


function display(data) {
    let table = document.getElementById("recordsTable");
    table.innerHTML = "";

    data.forEach(r => {
        let color = r.status === "Violated" ? "#ffe6e6" : "#e7f6ec";

        table.innerHTML += `
            <tr style="background:${color}">
                <td>${r.facility}</td>
                <td>${r.rule}</td>
                <td>${r.status}</td>
                <td>${r.date}</td>
            </tr>`;
    });
}


function showRecords() {
    display(records);
}


function undoLast() {
    if (undoStack.length === 0) {
        alert("Nothing to undo");
        return;
    }

    let last = undoStack.pop();
    records.pop();
    display(records);
}


function searchRecord() {

    let key = document.getElementById("searchInput").value.toLowerCase();

    let filtered = records.filter(r =>
        r.rule.toLowerCase().includes(key) ||
        r.facility.toLowerCase().includes(key)
    );

    display(filtered);
}