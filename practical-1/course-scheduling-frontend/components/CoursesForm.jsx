import {
  Button,
  Chip,
  Grid,
  Input,
  NumberInput,
  Paper,
  Title,
} from "@mantine/core";
import axios from "axios";
import React, { useEffect, useState } from "react";

export default function CoursesForm({ setSchedule }) {
  const [courses, setCourses] = useState([{ name: "", enrollment: 0 }]);
  const [times, setTimes] = useState([""]);
  const [rooms, setRooms] = useState([{ name: "", capacity: "" }]);
  const [preferences, setPreferences] = useState({});

  function setDefault() {
    setCourses([
      { name: "cs101", enrollment: 180 },
      { name: "cs412", enrollment: 80 },
      { name: "cs612", enrollment: 35 },
      { name: "cs630", enrollment: 40 },
    ]);

    setTimes([
      "MWF9",
      "MWF10",
      "MWF11",
      "MWF2",
      "TT9",
      "TT10:30",
      "TT2",
      "TT3:30",
    ]);

    setRooms([
      { name: "101", capacity: 25 },
      { name: "115", capacity: 50 },
      { name: "200", capacity: 250 },
    ]);

    setPreferences({
      cs101: ["MWF9", "MWF10", "MWF11", "TT9"],
      cs412: ["MWF9", "TT9", "TT10:30"],
      cs612: [],
      cs630: [],
    });
  }

  function addCourse() {
    setCourses([...courses, { name: "", enrollment: "" }]);
  }

  function removeCourse(removeIndex) {
    initializePreferences();
    setCourses(courses.filter((e, index) => index !== removeIndex));
  }

  function updateCourse(index, name = null, enrollment = null) {
    if (typeof name !== null)
      setCourses(courses.map((e, i) => (index === i ? { ...e, name } : e)));

    if (typeof enrollment !== null)
      setCourses(
        courses.map((e, i) => (index === i ? { ...e, enrollment } : e))
      );

    if (Object.keys(preferences).length > 1) {
      setPreferences({});
    }
  }

  function addTimes() {
    setTimes([...times, ""]);
  }

  function removeTimes(index) {
    setTimes(times.filter((e, i) => i !== index));
  }

  function updateTimes(index, time) {
    setTimes(times.map((e, i) => (i === index ? time : e)));
  }

  function addRooms() {
    setRooms([...rooms, { name: "", capacity: "" }]);
  }

  function removeRooms(index) {
    setRooms(rooms.filter((e, i) => i !== index));
  }

  function updateRooms(index, room = null, capacity = null) {
    if (typeof room !== null)
      setRooms(rooms.map((e, i) => (i === index ? { ...rooms, room } : e)));
    if (typeof capacity !== null)
      setRooms(rooms.map((e, i) => (i === index ? { ...rooms, capacity } : e)));
  }

  function initializePreferences() {
    let newPreferences = {};
    console.log(preferences);
    console.log(courses);

    courses.map(function (e) {
      newPreferences[e.name] = "";
    });

    setPreferences(newPreferences);
  }

  function submitHandler() {
    const data = {
      rooms: convertArrayToObject(rooms),
      courses: convertArrayToObject(courses),
      times: times,
      preferences: preferences,
    };
    console.log(data);

    axios
      .post("http://localhost:8080/api/schedule", data)
      .then((res) => {
        console.log(res.data);
        setSchedule(res.data);
      })
      .catch((e) => setSchedule(e.response.data));
  }

  return (
    <Paper shadow="xs" sx={{ padding: "2rem", width: "100%", height: "100%" }}>
      <Button onClick={setDefault}>Set Default</Button>
      <Title order={4} mt={25} mb={10}>
        Courses
      </Title>
      {/* Courses | Enrollment */}
      <Grid grow>
        {courses.map((course, index) => (
          <React.Fragment key={`course-${index}`}>
            <Grid.Col span={4}>
              <Input
                placeholder="Course Name"
                onChange={(e) => updateCourse(index, e.target.value, null)}
                value={course.name}
              ></Input>
            </Grid.Col>
            <Grid.Col span={4}>
              <NumberInput
                placeholder="Enrolled"
                onChange={(e) => updateCourse(index, null, e)}
                value={course.enrollment}
              ></NumberInput>
            </Grid.Col>
            <Grid.Col span={1}>
              <Button color={"green"} fullWidth onClick={addCourse}>
                +
              </Button>
            </Grid.Col>
            <Grid.Col span={1}>
              <Button color="red" fullWidth onClick={() => removeCourse(index)}>
                -
              </Button>
            </Grid.Col>
          </React.Fragment>
        ))}
      </Grid>

      {/* Times */}
      <Title order={4} mt={25} mb={10}>
        Times
      </Title>
      <Grid>
        {times.map((time, index) => (
          <React.Fragment key={`time-${index}`}>
            <Grid.Col span={4}>
              <Input
                placeholder="Time"
                onChange={(e) => updateTimes(index, e.target.value)}
                value={time}
              ></Input>
            </Grid.Col>
            <Grid.Col span={1}>
              <Button fullWidth onClick={addTimes} color="green">
                +
              </Button>
            </Grid.Col>
            <Grid.Col span={1}>
              <Button fullWidth onClick={() => removeTimes(index)} color="red">
                -
              </Button>
            </Grid.Col>
          </React.Fragment>
        ))}
      </Grid>

      {/* Rooms */}
      <Title order={4} mt={25} mb={10}>
        Rooms
      </Title>
      <Grid>
        {rooms.map((room, index) => (
          <React.Fragment key={`room-${index}`}>
            <Grid.Col span={5}>
              <Input
                placeholder="Room"
                onChange={(e) => updateRooms(index, e.target.value, null)}
                value={room.name}
              ></Input>
            </Grid.Col>
            <Grid.Col span={5}>
              <NumberInput
                placeholder="Capacity"
                onChange={(e) => updateRooms(index, null, e)}
                value={room.capacity}
              ></NumberInput>
            </Grid.Col>
            <Grid.Col span={1}>
              <Button fullWidth color="green" onClick={addRooms}>
                +
              </Button>
            </Grid.Col>
            <Grid.Col span={1}>
              <Button fullWidth color="red" onClick={() => removeRooms(index)}>
                -
              </Button>
            </Grid.Col>
          </React.Fragment>
        ))}
      </Grid>

      {/* Preferences */}
      <Title order={4} mt={25} mb={10}>
        Preferences
      </Title>
      <Grid>
        {Object.keys(preferences).length < 1 ? (
          <Grid.Col>
            <Button color="yellow" onClick={() => initializePreferences()}>
              Add Preferences
            </Button>
          </Grid.Col>
        ) : (
          Object.entries(preferences).map(([key, value], index) => (
            <React.Fragment key={`preferences-${index}`}>
              <Grid.Col span={4}>{key}</Grid.Col>
              <Grid.Col span={8}>
                <Chip.Group
                  multiple
                  value={value}
                  onChange={(e) => setPreferences({ ...preferences, [key]: e })}
                >
                  {times.map((e) => (
                    <Chip value={e}>{e}</Chip>
                  ))}
                </Chip.Group>
              </Grid.Col>
            </React.Fragment>
          ))
        )}
      </Grid>

      <Button mt={25} onClick={() => submitHandler()}>
        Submit
      </Button>
    </Paper>
  );
}

function convertArrayToObject(data) {
  let obj = {};

  if (data.length < 1) return obj;

  let key = Object.keys(data[0])[0];
  let value = Object.keys(data[0])[1];

  data.forEach((e) => {
    obj[e[key]] = e[value];
  });

  return obj;
}
