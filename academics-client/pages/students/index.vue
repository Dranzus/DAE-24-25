<template>
  <div v-if="error">Error: {{ error.message }}</div>
  <div v-else>
    <nuxt-link to="/students/create">Create a New Student</nuxt-link>
    <h2>Students</h2>
    <table>
      <thead>
      <tr><th>Username</th><th>Name</th><th>E-mail</th><th>Course</th></tr>
      </thead>
      <tbody>
      <tr v-for="student in students">
        <td>{{ student.username }}</td>
        <td>{{ student.name }}</td>
        <td>{{ student.email }}</td>
        <td>{{ student.courseName }}</td>
        <td><nuxt-link :to="`/students/${student.username}`">Details</nuxt-link> </td>
      </tr>
      </tbody>
    </table>
  </div>
  <button @click.prevent="refresh">Refresh Data</button>
</template>

<script setup>
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: students, error, refresh } = await useFetch(`${api}/students`)
</script>