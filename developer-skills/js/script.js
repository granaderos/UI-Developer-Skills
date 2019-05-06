var baseAPIURL = "http://localhost:8080/api-developers/rest";

$(document).ready(function() {
    $("#formTabs").tabs();
    $("#tableTabs").tabs();

    $("#formSkill").submit(function(e) {
        e.preventDefault();
        var skill = $("#skill").val();
        console.log("Skill to add: " + skill);
        var skillData = {
            "skill": skill
        }
        $.ajax({
            type: "POST",
            url: baseAPIURL + "/skills/add",
            data: JSON.stringify(skillData),
            contentType: "application/json",
            success: function(data) {
                $("#skill").val("");
                displaySkills();
                alert(data);

            },
            error: function(data) {
                console.log("Error in adding skill " + JSON.stringify(data));
            }
        });
    });

    $("#formDeveloper").submit(function(e) {
        e.preventDefault();
        var firstName = $("#firstName").val();
        var middleName = $("#middleName").val();
        var lastName = $("#lastName").val();
        var birthDate = $("#birthDate").val();
        var position = $("#position").val();

        var devData = {
            "firstName": firstName,
            "middleName": middleName,
            "lastName": lastName,
            "birthDate": birthDate,
            "position": position
        }
        $.ajax({
            type: "POST",
            url: baseAPIURL + "/developers/add",
            data: JSON.stringify(devData),
            contentType: "application/json",
            success: function(data) {
                displayDevelopers();
                alert(data);

                $("#firstName").val("");
                $("#middleName").val("");
                $("#lastName").val("");
                $("#birthDate").val("");
                $("#position").val("");
            },
            error: function(data) {
                console.log("Error in adding a developer " + JSON.stringify(data));
            }
        });
    });

    $("#divAddSkillAssessment").submit(function(e) {
        e.preventDefault();
        var skillId = $("#saSkill").val();
        var devId = $("#saDeveloper").val();
        var monthsOfExp = $("#saMonthsOfExperience").val();
        var skillLevel = $("#saSkillLevel").val();

        var saData = {
            "skill": [
                {
                    "skillId": skillId
                }
            ],
            "developer": {
                "developerId": devId
            },
            "monthsOfExperience": monthsOfExp,
            "skillLevel": skillLevel
        }
        $.ajax({
            type: "POST",
            url: baseAPIURL + "/skillAssessments/add",
            data: JSON.stringify(saData),
            contentType: "application/json",
            success: function(data) {
                $("#saSkill").val("");
                $("#saDeveloper").val("");
                $("#saMonthsOfExperience").val("");
                $("#saSkillLevel").val("");

                displaySkillCapabilityReport();
                alert(data);

            },
            error: function(data) {
                console.log("Error in adding skill assessment " + JSON.stringify(data));
            }
        });
    });

    displaySkills();
    displayDevelopers();
    displaySkillLevels();
    displaySkillCapabilityReport();
});

function displaySkillCapabilityReport() {
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/skills/capabilityReport",
        success: function(data) {
            var reports = "";
            for(var i = 0; i < data.length; i++) {
                var rows = "";
                var row = data[i];
                rows += "<tr>"
                        + "<td>"+row.skill+"</td>";

                for(var j = 0; j < row.report.length; j++) {
                    rows += "<td>"+row.report[j].count+"</td>";
                }
                rows += "</tr>";
                reports += rows;
            }
            $("#tbodySkillCapabilityReport").html(reports);
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });
}

function displaySkills() {
    // display skills
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/skills",
        success: function(data) {
            var skills = data;
            $("#saSkill").html("");
            $("#tbodySkills").html("");
            for(var i = 0; i < skills.length; i++) {
                $("#tbodySkills").append("<tr><td>"+skills[i].skill+"</td></tr>");
                $("#saSkill").append("<option value="+skills[i].skillId+">"+skills[i].skill+"</option>");

            }
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });
}

function displayDevelopers() {
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/developers",
        success: function(data) {
            var devs = data;
            $("#saDeveloper").html("");
            $("#tbodyDevelopers").html("");
            for(var i = 0; i < devs.length; i++) {
                $("#tbodyDevelopers").append("<tr>"
                       + "<td>"+devs[i].firstName+"</td>"
                       + "<td>"+ devs[i].middleName+"</td>"
                       + "<td>"+devs[i].lastName+"</td>"
                       + "<td>"+devs[i].birthDate+"</td>"
                       + "<td>"+devs[i].position+"</td>"
                    +"</tr>");
                $("#saDeveloper").append("<option value="+devs[i].developerId+">"+devs[i].firstName+" "+ devs[i].middleName+" "+devs[i].lastName+"</option>");

            }
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });

}

function displaySkillLevels() {
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/skills/levels",
        success: function(data) {
            var levels = data;
            $("#saSkillLevel").html("");
            var thead = "<tr>"
            for(var i = 0; i < levels.length; i++) {
                $("#saSkillLevel").append("<option value="+levels[i]+">"+levels[i]+"</option>");
                thead += "<th>"+i+":"+levels[i]+"</th>";
            }
            thead += "</tr>";
            $("#theadSkillCapabilityReport").html(thead);
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });
}